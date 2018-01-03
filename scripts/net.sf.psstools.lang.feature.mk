
FEATURE := net.sf.psstools.lang.feature
FEATURE_SRCDIR := $(PSSTOOLS_DIR)/psstools
FEATURE_DIR := $(FEATURE_SRCDIR)/features/$(FEATURE)
FEATURE_VERSION := 0.0.1
FEATURE_PRE_BUILD_TARGETS := $(FEATURE)_mw2e


include $(ECLIPSESCRIPTS_DIR)/mkfiles/eclipsefeature.mk

ifneq (1,$(RULES))
ECLIPSE_SDK_PKGS += xtext

else # Rules
$(FEATURE)_mw2e : 
	$(Q)$(ECLIPSE_ANT) \
		-buildfile $(call NATIVE_PATH,$(PSSTOOLS_SCRIPTS_DIR)/mw2e_net.sf.psstools.lang.feature.xml) \
		-DbuildDirectory=$(call NATIVE_PATH,$(net.sf.psstools.lang.feature_BUILDDIR)/build) \
		-Dsrcdir=$(call NATIVE_PATH,$(PSSTOOLS_DIR)/psstools) \
		-verbose \
		build

endif

