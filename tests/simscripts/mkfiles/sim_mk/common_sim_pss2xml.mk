
#********************************************************************
#* Compile rules
#********************************************************************


ifeq (true,$(QUIET))
REDIRECT:= >/dev/null 2>&1
else
endif

build : $(LIB_TARGETS) 

PSSFILES:=$(call get_plusarg,PSSFILE,$(PLUSARGS))


run : 
	$(SIM_DIR)/../../build/psstools/bin/pss2xml \
		-o model.xml $(PSSFILES) > run.out

