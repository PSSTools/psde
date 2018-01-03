
PSSTOOLS_SCRIPTS_DIR := $(dir $(lastword $(MAKEFILE_LIST)))
PSSTOOLS_SCRIPTS_DIR := $(shell cd $(PSSTOOLS_SCRIPTS_DIR) ; pwd)
PSSTOOLS_DIR := $(shell cd $(PSSTOOLS_SCRIPTS_DIR)/.. ; pwd)

BUILD_DIR := $(PSSTOOLS_DIR)/build
PACKAGES_DIR := $(PSSTOOLS_DIR)/packages
BUILD_TOOLS_DIR := $(BUILD_DIR)/tools

# MK_INCLUDES += $(PSSTOOLS_SCRIPTS_DIR)/pssc.mk
# MK_INCLUDES += $(PSSTOOLS_SCRIPTS_DIR)/psde.mk
MK_INCLUDES += $(PSSTOOLS_SCRIPTS_DIR)/net.sf.psstools.lang.feature.mk
# MK_INCLUDES += $(PSSTOOLS_SCRIPTS_DIR)/net.sf.psstools.pssc.feature.mk

FEATURE_VERSION=0.0.1
SF_FRS_URL=$(SF_USERNAME),psstools@frs.sourceforge.net
SF_WEB_URL=$(SF_USERNAME),psstools@web.sourceforge.net
SF_UPDATE_URL=$(SF_USERNAME),psstools@web.sourceforge.net:htdocs
SF_PSSTOOLS_DIR=/home/frs/project/p/ps/psstools
BINTRAY_ID=-u$(BINTRAY_USERNAME):$(BINTRAY_API_KEY)
BINTRAY_UPDATE_URL=https://api.bintray.com/content/mballance/psstools/update-site/$(FEATURE_VERSION)/$(FEATURE_VERSION)
BINTRAY_RELEASE_URL=https://api.bintray.com/content/mballance/psstools/release/$(FEATURE_VERSION)/$(FEATURE_VERSION)

PSSTOOLS_BUILD_DIR=$(BUILD_DIR)/net.sf.psstools.lang.feature

define bintray_upload_update
curl -T $(1) $(BINTRAY_ID) $(BINTRAY_UPDATE_URL)/$(1)
endef

release : net.sf.psstools.lang.feature

upload :
	mkdir -p $(PSSTOOLS_BUILD_DIR)/update-site
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; unzip -o $(PSSTOOLS_BUILD_DIR)/psstools-release-$(FEATURE_VERSION).zip
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		echo "-mkdir update-site" > cmds ; \
		echo "cd update-site" >> cmds ; \
		echo "put content.jar" >> cmds ; \
		echo "put artifacts.jar" >> cmds
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; sftp -b cmds $(SF_WEB_URL):htdocs
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		$(call bintray_upload_update,features/net.sf.psstools.lang.feature_$(FEATURE_VERSION).jar)
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		$(call bintray_upload_update,plugins/net.sf.psstools.lang_$(FEATURE_VERSION).jar)
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		$(call bintray_upload_update,plugins/net.sf.psstools.lang.ui_$(FEATURE_VERSION).jar)
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		$(call bintray_upload_update,plugins/net.sf.psstools.lang.ide_$(FEATURE_VERSION).jar)
	curl -X POST $(BINTRAY_ID) https://api.bintray.com/content/mballance/psstools/update-site/$(FEATURE_VERSION)/publish
	rm -rf $(PSSTOOLS_BUILD_DIR)/update-site
	cd $(PSSTOOLS_BUILD_DIR) ; \
		echo "cd $(SF_PSSTOOLS_DIR)" > cmds ; \
		echo "-mkdir release" >> cmds ; \
		echo "cd release" >> cmds ; \
		echo "-mkdir $(FEATURE_VERSION)" >> cmds ; \
		echo "cd $(FEATURE_VERSION)" >> cmds ; \
		echo "put psstools-$(FEATURE_VERSION).jar" >> cmds
	cd $(PSSTOOLS_BUILD_DIR) ; sftp -b cmds $(SF_FRS_URL)
	rm -f $(PSSTOOLS_BUILD_DIR)/cmds




include $(PSSTOOLS_DIR)/eclipsescripts/mkfiles/eclipsescripts.mk


