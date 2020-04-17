

srcdir = \projects\doc2html

jarname = doc2html.jar

rootdir = $(srcdir)\root

onejardir = $(srcdir)\OneJar

onejarfile = one-jar-boot-0.97.jar

packagepath = com/mythosseed/doc2html

releasedir = $(srcdir)\Release

onejarout = $(releasedir)\$(jarname)

onejarmanifest = $(onejardir)\d2h_MANIFEST.mf

classdir = $(srcdir)\out\production\doc2html\$(packagepath)

srcjardir = $(srcdir)\out\artifacts\doc2html_jar

srcjarfile = $(srcjardir)\$(jarname)

classfiles = $(classdir)\doc2html.class $(classdir)\StripSearch.class \
	$(classdir)/proxyWriter.class $(classdir)/mythosStrings.class

srcjavadir = $(srcdir)\src\$(packagepath)

javafiles = $(srcjavadir)\doc2html.java \
	$(srcjavadir)\proxyWriter.java \
	$(srcjavadir)\StripSearch.java \
	$(srcjavadir)\mythosStrings.java
	
make all:
	cls
	@echo USAGE: Compile the project in Intellij,
	@echo and then use this makefile to package 
	@echo the resulting files into a One-JAR(tm) 
	@echo package with the command: MAKE OneJar
	@echo *** this makefile is for windows ***
	@echo *** it will not run correctly ******
	@echo *** on Linux, Mac, or Unix *********

	
# DEPENDENCIES
	
$(classfiles): $(javafiles)

$(srcjarfile): $(classfiles)

$(onejarout): $(srcjarfile)

# MAKE THE ONEJAR FILE
# JAR will not output to a specific directory, use 7z	
onejar: $(onejarout) $(srcjarfile) $(classfiles)
	-del /Q $(onejarout) 
	-rmdir /S /Q $(rootdir)
	7z x -o$(rootdir) $(onejardir)\$(onejarfile)
	rmdir /S /Q $(rootdir)\src
	mkdir $(rootdir)\main
	mkdir $(rootdir)\lib
	move /Y $(srcjarfile) $(rootdir)\main\$(jarname)
	move /Y $(srcjardir) $(rootdir)\lib
	cd $(rootdir) & jar -cvmf $(onejarmanifest) $(onejarout) .
	move /Y $(rootdir)\lib $(srcjardir)
	move /Y $(rootdir)\main\doc2html.jar $(srcjardir)
	-rmdir /S /Q $(rootdir)

nuke:
	del /F $(onejarout)
	
	

	
	
	
