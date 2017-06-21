
PSSTOOLS_SCRIPTS_DIR := $(dir $(lastword $(MAKEFILE_LIST)))
PSSTOOLS_SCRIPTS_DIR := $(shell cd $(PSSTOOLS_SCRIPTS_DIR) ; pwd)
PSSTOOLS_DIR := $(shell cd $(PSSTOOLS_SCRIPTS_DIR)/.. ; pwd)

BUILD_DIR := $(PSSTOOLS_DIR)/build
PACKAGES_DIR := $(PSSTOOLS_DIR)/packages
BUILD_TOOLS_DIR := $(BUILD_DIR)/tools

MK_INCLUDES += $(PSSTOOLS_SCRIPTS_DIR)/pssc.mk
# MK_INCLUDES += $(PSSTOOLS_SCRIPTS_DIR)/psde.mk
include $(PSSTOOLS_DIR)/eclipsescripts/mkfiles/eclipsescripts.mk
