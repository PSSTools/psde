/****************************************************************************
 * pss2xml.cpp
 *
 * Wrapper around the PSS2XML tool, which is Java based
 ****************************************************************************/

#if defined(_WIN32)
#define PS ';'
#else
#define PS ':'
#endif

#include <string>
#include <vector>
#include <stdio.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdint.h>
#include <sys/types.h>
#if defined(_WIN32)
#error "Need getcwd support in Windows"
#else
#include <sys/stat.h>
#include <unistd.h>
#endif

std::string getwd() {
	char buf[1024];
	::getcwd(buf, sizeof(buf));

	return buf;
}

bool exists(const std::string &path) {
	struct stat sb;

	int ret = stat(path.c_str(), &sb);

	return (ret != -1 && (S_ISREG(sb.st_mode) || S_ISDIR(sb.st_mode)));
}

void setwd(const std::string &path) {
	::chdir(path.c_str());
}

std::string dirname(const std::string &path) {
	std::size_t fs_idx = path.rfind('/');

	if (fs_idx != std::string::npos) {
		return path.substr(0, fs_idx);
	} else {
		return "";
	}
}

std::string find_exedir(const std::string &exe) {
	std::string dir = dirname(exe);

	if (dir.size() > 3 && (dir.at(0) == '/' || (
			isalpha(dir.at(0)) && dir.at(1) == ':' && dir.at(2) == '/'))) {
		// Absolute path
		fprintf(stdout, "Absolute path: %s\n", exe.c_str());


		return dir;
	} else {
		if (dir == "") {
			// We might have something from PATH
			fprintf(stdout, "Look in path: %s\n", exe.c_str());
			uint32_t pos=0, npos;
			std::string path = ::getenv("PATH");

			while (true) {
				npos = path.find(PS, pos);

				std::string pathseg = (npos == std::string::npos)?
						path.substr(pos):path.substr(pos,(npos-pos));

				if (exists(pathseg + "/" + exe)) {
					return pathseg;
				}

				if (npos == std::string::npos) {
					break;
				}
				pos = npos+1;
			}
		} else {
			// We either have an absolute path or a relative one
			std::string cwd = getwd();
			std::string absdir = cwd + '/' + dir;
			setwd(absdir);
			absdir = getwd();
			setwd(cwd);

			return absdir;
		}
	}
	return dir;
}

std::string normalize(const std::string &path) {
	std::string ret(path);

	for (uint32_t i=0; i<ret.size(); i++) {
		if (ret.at(i) == '\\') {
			ret.replace(i, 1, "/");
		}
	}

	return ret;
}

std::string toWinPath(const std::string &path) {
	if (path.substr(0, 10) == "/cygdrive/") {
		// Convert to a <letter>:/ format
		char drive[2];
		drive[0] =path.at(10);
		drive[1] = 0;
		return std::string(drive) + ":" + path.substr(11);
	}
	return path;
}

int main(int argc, char **argv) {

	// First, locate where we're installed
	std::string exedir = find_exedir(normalize(argv[0]));
	std::string libdir = dirname(exedir) + "/lib";

#ifdef __CYGWIN__
	libdir = toWinPath(libdir);
#endif

#ifdef _WIN32
#error Need Windows exec
#else // Linux, Cygwin, etc
	std::vector<std::string> args;
	args.push_back("java");
	args.push_back("-classpath");
	args.push_back(libdir + "/pss2xml.jar");
	args.push_back("net.sf.psstools.lang.pss2xml.PSS2XML");

	for (int i=1; i<argc; i++) {
		args.push_back(toWinPath(argv[i]));
	}

	char **e_argv = new char *[args.size()+1];
	for (uint32_t i=0; i<args.size(); i++) {
		e_argv[i] = const_cast<char *>(args.at(i).c_str());
	}
	e_argv[args.size()] = 0;

//	for (uint32_t i=0; i<args.size(); i++) {
//		fprintf(stdout, "    ARGV[%d]=%s\n", i, args.at(i).c_str());
//	}

	int ret = execvp("javaw", e_argv);

	// The Windows platform will have javaw, while we all platforms
	// will have java.
	if (ret == -1) {
		ret = execvp("java", e_argv);
		exit(1);
	}
#endif

	fprintf(stdout, "exedir: %s\n", exedir.c_str());

}
