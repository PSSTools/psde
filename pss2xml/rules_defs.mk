

ifneq (1,$(RULES))

PSS2XML_DIR := $(PSSTOOLS_DIR)/pss2xml
PSS2XML_DIR_A := $(PSSTOOLS_DIR_A)/pss2xml
PSS2XML_BUILDDIR := $(BUILDDIR)/pss2xml

ifeq (Cygwin,$(UNAME_O))
PSS2XML_BUILDDIR_A := $(shell cygpath -w $(PSS2XML_BUILDDIR) | sed -e 's%\\%/%g')
else
PSS2XML_BUILDDIR_A := $(PSS2XML_BUILDDIR)
endif

PSS2XML_EXE_TARGETS += $(BINDIR)/pss2xml$(EXEEXT)
EXE_TARGETS += $(PSS2XML_EXE_TARGETS)
PSS2XML_LIB_TARGETS += $(LIBDIR)/pss2xml.jar
LIB_TARGETS += $(PSS2XML_LIB_TARGETS)

PSS2XML_SRC=pss2xml.cpp

else

$(LIBDIR)/pss2xml.jar : $(BUILDDIR)/pss2xml/pss2xml.jar
	$(MKDIRS)
	$(Q)cp $^ $@
	
$(BINDIR)/pss2xml$(EXEEXT) : $(foreach o,$(PSS2XML_SRC:.cpp=.o),$(PSS2XML_BUILDDIR)/$(o))
	$(MKDIRS)
	$(DO_CXXLINK)

LANG_PLUGIN := $(PSSTOOLS_DIR)/psstools/plugins/net.sf.psstools.lang
PSS2XML_DEPS := $(shell find $(LANG_PLUGIN) -name '*.java'; find $(LANG_PLUGIN) -name '*.xtext')

$(BUILDDIR)/pss2xml/pss2xml.jar : $(PSS2XML_DEPS)
	$(Q)$(call eclipse_ant,$(PSS2XML_DIR_A)/build.xml,build -DbuildDirectory=$(PSS2XML_BUILDDIR_A))

$(PSS2XML_BUILDDIR)/%.o : $(PSS2XML_DIR)/%.cpp
	$(MKDIRS)
	$(DO_CXX)
	
pss2xml : $(PSS2XML_EXE_TARGETS) $(PSS2XML_LIB_TARGETS)
	
clean-pss2xml:
	$(Q)rm -rf $(PSS2XML_BUILDDIR)

endif
