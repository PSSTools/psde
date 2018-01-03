

PRODUCT := psde
PRODUCT_FILE := /net.sf.psstools.pssc.feature/pssc.product
PRODUCT_SRCDIR := $(PSSTOOLS_DIR)/psstools
PRODUCT_FEATURE_DIR := $(PSSTOOLS_DIR)/psstools/features/net.sf.psstools.pssc.feature
PRODUCT_VERSION := 0.0.1
# PRODUCT_PRE_BUILD_TARGETS += $(PRODUCT)pssc_mw2e

include $(ECLIPSESCRIPTS_DIR)/mkfiles/eclipseproduct.mk

ifneq (1,$(RULES))
ECLIPSE_SDK_PKGS += xtext

else # Rules

pssc_mw2e : 
	$(Q)$(ECLIPSE_ANT) -buildfile $(call NATIVE_PATH,$(PSSTOOLS_SCRIPTS_DIR)/mw2e_build.xml) \
		-DbuildDirectory=$(call NATIVE_PATH,$($(PRODUCT)_BUILDDIR)/product) build
		
endif

