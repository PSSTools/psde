
include $(SIMSCRIPTS_DIR)/mkfiles/plusargs.mk

MK_INCLUDES += $(SIM_DIR)/../../scripts/rules_defs.mk
MK_INCLUDES += $(SIM_DIR)/../../scripts/psstools_mkfiles.mk

BUILD_TARGETS += $(LIB_TARGETS) $(EXE_TARGETS) $(INST_TARGETS)
RUN_TARGETS += run_test

BUILDDIR := $(BUILD_DIR)
BUILDRESULT := $(BUILD_DIR)/psstools

CXXFLAGS += -g
CFLAGS += -g

include $(SIMSCRIPTS_DIR)/mkfiles/common_sim.mk

PSSFILE := $(call get_plusarg,PSSFILE,$(PLUSARGS))

ifeq (true,$(call have_plusarg,valgrind,$(PLUSARGS)))
VALGRIND := valgrind --tool=memcheck
endif


run_test :
	echo "Hello from run_test"
	echo "have_plusarg: $(call have_plusarg,valgrind,$(PLUSARGS))"
	$(BUILD_DIR)/psstools/bin/pss2xml $(PSSFILE) -o model.xml > pss2xml.out 2>&1
	$(VALGRIND) $(BINDIR)/pssxml2xml model.xml -o model_o.xml > pssxml2xml.out 2>&1
	$(VALGRIND) $(BINDIR)/validatepssxml model_o.xml > validatepssxml.out 2>&1

