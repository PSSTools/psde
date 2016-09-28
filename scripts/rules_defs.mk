
ifneq (1,$(RULES))

SCRIPTS_DIR := $(shell cd $(dir $(lastword $(MAKEFILE_LIST))); pwd)

UNAME_O := $(shell uname -o)
UNAME_M := $(shell uname -m)

PSSTOOLS_DIR := $(dir $(SCRIPTS_DIR))

ifeq (Cygwin,$(UNAME_O))
SCRIPTS_DIR := $(shell cygpath -w $(SCRIPTS_DIR) | sed -e 's%\\%/%g')
os:=win32
ws:=win32
else
ifeq ("GNU/Linux","$(UNAME_O)")
os:=linux
ws:=gtk
endif
endif

ifeq (x86_64,$(UNAME_M))
arch:=$(UNAME_M)
else
arch:=i686
endif

PSSTOOLS_DIR_A := $(dir $(SCRIPTS_DIR))

ifeq (,$(BUILDDIR))
BUILDDIR := $(PSSTOOLS_DIR)/build
endif

ifeq (,$(BUILDRESULT))
BUILDRESULT := $(PSSTOOLS_DIR)/build/psstools
endif

BINDIR := $(BUILDRESULT)/bin
LIBDIR := $(BUILDRESULT)/lib
INCDIR := $(BUILDRESULT)/include

OBJDIR := $(BUILDDIR)/obj


ifeq (Cygwin,$(UNAME_O))
ECLIPSE_C := $(ECLIPSE_HOME)/eclipsec
EXEEXT:=.exe
else
ECLIPSE_C := $(ECLIPSE_HOME)/eclipse
EXEEXT:=
endif

ECLIPSE_CMD := $(ECLIPSE_C) -nosplash --launcher.suppressErrors

define eclipse_ant
$(ECLIPSE_CMD) -application org.eclipse.ant.core.antRunner \
  -buildfile $(1) -Dos=$(os) -Dws=$(ws) -Darch=$(arch) $(2)
endef

define MKDIRS
$(Q)if test ! -d $(BINDIR); then mkdir -p $(BINDIR); fi
$(Q)if test ! -d $(LIBDIR); then mkdir -p $(LIBDIR); fi
$(Q)if test ! -d $(INCDIR); then mkdir -p $(INCDIR); fi
endef

ifeq (,$(VERBOSE))
VERBOSE:=false
endif

ifeq (false,$(VERBOSE))
Q:=@
TMSG:=@echo

define MK_AR 
@rm -f $@
@echo "$(AR) $@"
@$(AR) cq $@ $^
endef

else # Verbose mode
TMSG:=#

define MK_AR 
rm -f $@
$(AR) vcq $@ $^
endef

endif

define DO_CXX
$(TMSG) "CXX $^"
$(Q)if test ! -d `dirname $@`; then mkdir -p `dirname $@`; fi
$(Q)$(CXX) -c $(CXXFLAGS) -o $@ $^
endef

define DO_CXXLINK
$(TMSG) "CXXLINK $@"
$(Q)if test ! -d `dirname $@`; then mkdir -p `dirname $@`; fi
$(Q)$(CXX) $(CXXFLAGS) -o $@ $^
endef

define DO_INST
$(TMSG) "install: $@"
$(Q)if test ! -d `dirname $@`; then mkdir -p `dirname $@`; fi
$(Q)cp $^ $@
endef

else

# $(INCDIR)/%.h : %.h

endif