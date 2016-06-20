

ifneq (1,$(RULES))

PSS2XML_DIR := $(PSSTOOLS_DIR)/pss2xml
PSS2XML_BUILDDIR := $(BUILDDIR)/pss2xml

ifeq (Cygwin,$(UNAME_O))
PSS2XML_BUILDDIR := $(shell cygpath -w $(PSS2XML_BUILDDIR) | sed -e 's%\\%/%g')
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

$(BUILDDIR)/pss2xml/pss2xml.jar :
	$(Q)$(call eclipse_ant,$(PSS2XML_DIR)/build.xml,build -DbuildDirectory=$(PSS2XML_BUILDDIR))

$(PSS2XML_BUILDDIR)/%.o : $(PSS2XML_DIR)/%.cpp
	$(MKDIRS)
	$(DO_CXX)
	
pss2xml : $(PSS2XML_EXE_TARGETS) $(PSS2XML_LIB_TARGETS)
	
clean-pss2xml:
	$(Q)rm -rf $(PSS2XML_BUILDDIR)

endif
