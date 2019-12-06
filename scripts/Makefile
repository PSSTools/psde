

PSSTOOLS_SCRIPTS_DIR := $(dir $(lastword $(MAKEFILE_LIST)))
PSSTOOLS_SCRIPTS_DIR := $(shell cd $(PSSTOOLS_SCRIPTS_DIR) ; pwd)
PSSTOOLS_DIR := $(shell cd $(PSSTOOLS_SCRIPTS_DIR)/.. ; pwd)

include $(PSSTOOLS_DIR)/etc/psde.info
FEATURE_VERSION:=$(version)

BUILD_DIR := $(PSSTOOLS_DIR)/build
PACKAGES_DIR := $(PSSTOOLS_DIR)/packages
BUILD_TOOLS_DIR := $(BUILD_DIR)/tools

# MK_INCLUDES += $(PSSTOOLS_SCRIPTS_DIR)/org.psstools.psde.lang.feature.mk

SF_FRS_URL=$(SF_USERNAME),psstools@frs.sourceforge.net
SF_WEB_URL=$(SF_USERNAME),psstools@web.sourceforge.net
SF_UPDATE_URL=$(SF_USERNAME),psstools@web.sourceforge.net:htdocs
SF_PSSTOOLS_DIR=/home/frs/project/p/ps/psstools
BINTRAY_ID=-u$(BINTRAY_USERNAME):$(BINTRAY_API_KEY)
BINTRAY_UPDATE_URL=https://api.bintray.com/content/mballance/psstools/psde/$(FEATURE_VERSION)/psde/$(FEATURE_VERSION)/update-site
BINTRAY_RELEASE_URL=https://api.bintray.com/content/mballance/psstools/psde/$(FEATURE_VERSION)/psde/$(FEATURE_VERSION)/release
BINTRAY_SITE_URL=https://api.bintray.com/content/mballance/psstools/psde

PSSTOOLS_BUILD_DIR=$(BUILD_DIR)/org.psstools.psde.lang.feature

define bintray_upload_update
curl -T $(1) $(BINTRAY_ID) $(BINTRAY_UPDATE_URL)/$(1)
endef

define bintray_upload_release
curl -T $(1) $(BINTRAY_ID) $(BINTRAY_RELEASE_URL)/$(1)
endef

define bintray_upload_site
curl -T $(1) $(BINTRAY_ID) $(BINTRAY_SITE_URL)/$(1)
endef

release : 
	cd $(PSSTOOLS_SCRIPTS_DIR)/.. ; \
		mvn tycho-versions:set-version \
		-DnewVersion=$(version)-SNAPSHOT -DoldVersion='*'
	cd $(PSSTOOLS_SCRIPTS_DIR)/.. ; mvn clean verify

upload :
	mkdir -p $(PSSTOOLS_BUILD_DIR)/update-site
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
          unzip -o $(PSSTOOLS_BUILD_DIR)/psde-release-$(FEATURE_VERSION).zip
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		$(call bintray_upload_site,content.jar)
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		$(call bintray_upload_site,artifacts.jar)
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		$(call bintray_upload_update,features/org.psstools.psde.lang.feature_$(FEATURE_VERSION).jar)
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		$(call bintray_upload_update,plugins/org.psstools.psde.lang_$(FEATURE_VERSION).jar)
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		$(call bintray_upload_update,plugins/org.psstools.psde.lang.ui_$(FEATURE_VERSION).jar)
	cd $(PSSTOOLS_BUILD_DIR)/update-site ; \
		$(call bintray_upload_update,plugins/org.psstools.psde.lang.ide_$(FEATURE_VERSION).jar)
	cd $(PSSTOOLS_BUILD_DIR) ; \
		$(call bintray_upload_release,psde-$(FEATURE_VERSION).jar)
	cd $(PSSTOOLS_BUILD_DIR) ; \
		$(call bintray_upload_release,psde-release-$(FEATURE_VERSION).zip)
	cd $(PSSTOOLS_BUILD_DIR) ; \
		$(call bintray_upload_release,ChangeLog.txt)
	curl -X POST $(BINTRAY_ID) https://api.bintray.com/content/mballance/psstools/psde/$(FEATURE_VERSION)/publish
	rm -rf $(PSSTOOLS_BUILD_DIR)/update-site


