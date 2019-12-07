

PSSTOOLS_SCRIPTS_DIR := $(dir $(lastword $(MAKEFILE_LIST)))
PSSTOOLS_SCRIPTS_DIR := $(shell cd $(PSSTOOLS_SCRIPTS_DIR) ; pwd)
PSSTOOLS_DIR := $(shell cd $(PSSTOOLS_SCRIPTS_DIR)/.. ; pwd)

include $(PSSTOOLS_DIR)/etc/psde.info
FEATURE_VERSION:=$(version)

BUILD_DIR := $(PSSTOOLS_DIR)/build
PACKAGES_DIR := $(PSSTOOLS_DIR)/packages
BUILD_TOOLS_DIR := $(BUILD_DIR)/tools

SF_FRS_URL=$(SF_USERNAME),psstools@frs.sourceforge.net
SF_WEB_URL=$(SF_USERNAME),psstools@web.sourceforge.net
SF_UPDATE_URL=$(SF_USERNAME),psstools@web.sourceforge.net:htdocs
SF_PSSTOOLS_DIR=/home/frs/project/p/ps/psstools
BINTRAY_ID=-u$(BINTRAY_USERNAME):$(BINTRAY_API_KEY)
BINTRAY_PSDE_URL=https://api.bintray.com/content/mballance/psstools/psde/
BINTRAY_UPDATE_URL=https://api.bintray.com/content/mballance/psstools/psde/$(version)/psde/$(version)/update-site
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

PSDE_REPO_DIR=$(PSSTOOLS_DIR)/releng/org.psstools.psde.update/target/repository
#curl -T $$file $(BINTRAY_ID) $(BINTRAY_PSDE_URL)/$(version).$${qualifier}/psde/$(version).$${qualifier}/update/$$file ; \

upload-release :
	cd $(PSDE_REPO_DIR) ; \
	qualifier=`ls features/*.jar | sed -e 's/^.*org.psstools.psde.lang.feature_[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*\.\([0-9][0-9]*\).*/\1/'`; \
	for file in *.jar *.xml.xz *.index features/*.jar plugins/*.jar; do \
		echo "upload $$file"; \
		curl -T $$file $(BINTRAY_ID) $(BINTRAY_SITE_URL)/update/$$file;publish=1;override=1 ; \
	done ; \

upload-testing :
	cd $(PSDE_REPO_DIR) ; \
	qualifier=`ls features/*.jar | sed -e 's/^.*org.psstools.psde.lang.feature_[0-9][0-9]*\.[0-9][0-9]*\.[0-9][0-9]*\.\([0-9][0-9]*\).*/\1/'`; \
	for file in *.jar *.xml.xz *.index features/*.jar plugins/*.jar; do \
		echo "upload $$file"; \
		curl -T $$file $(BINTRAY_ID) $(BINTRAY_SITE_URL)/update-testing/$$file;publish=1;override=1 ; \
	done ; \

#	curl -X POST $(BINTRAY_ID) https://api.bintray.com/content/mballance/psstools/psde/$(version).$${qualifier}/publish


