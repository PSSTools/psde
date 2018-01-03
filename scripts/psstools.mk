
FEATURE := net.sf.psstools.lang.feature


include $(ECLIPSESCRIPTS_DIR)/mkfiles/eclipseproduct.mk

ifneq (1,$(RULES))
ECLIPSE_SDK_PKGS += xtext
else # Rules
$(PRODUCT)_pssc_mw2e : 
	$(Q)$(ECLIPSE_ANT) -buildfile $(call NATIVE_PATH,$(PSSTOOLS_SCRIPTS_DIR)/mw2e_build.xml) \
		-DbuildDirectory=$(call NATIVE_PATH,$($(PRODUCT)_BUILDDIR)/product) build

endif

