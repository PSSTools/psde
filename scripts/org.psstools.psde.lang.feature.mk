
FEATURE := org.psstools.psde.lang.feature
FEATURE_SRCDIR := $(PSSTOOLS_DIR)/psde
FEATURE_DIR := $(FEATURE_SRCDIR)/features/$(FEATURE)
# FEATURE_VERSION := 0.0.1
FEATURE_PRE_BUILD_TARGETS := $(FEATURE)_mw2e


include $(ECLIPSESCRIPTS_DIR)/mkfiles/eclipsefeature.mk

ifneq (1,$(RULES))
ECLIPSE_SDK_PKGS += xtext

else # Rules
$(FEATURE)_mw2e : 
	$(Q)$(ECLIPSE_ANT) \
		-buildfile $(call NATIVE_PATH,$(PSSTOOLS_SCRIPTS_DIR)/mw2e_org.psstools.psde.lang.feature.xml) \
		-DbuildDirectory=$(call NATIVE_PATH,$(org.psstools.psde.lang.feature_BUILDDIR)/build) \
		-Dfeature.version=$(FEATURE_VERSION) \
		-Dsrcdir=$(call NATIVE_PATH,$(PSSTOOLS_DIR)/psde) \
		-Dos=$(osgi_os) -Dws=$(osgi_ws) -Darch=$(osgi_arch) \
		-verbose \
		build

endif

