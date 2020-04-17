

srcdir = /projects/doc2html

root = $(srcdir)/root

onejardir = $(srcdir)/OneJar

onejarfile = one-jar-boot-0.97.jar

packagepath = com/mythosseed/doc2html

releasedir = $(srcdir)\Release

onejarout = $(releasedir)/doc2html.jar

onejarmanifest = $(releasedir)/d2h_MANIFEST.mf

classdir = $(srcdir)\out\production\doc2html/$(packagepath)

srcjardir = $(srcdir)\out\production\artifacts/doc2html_jar

srcjarfile = $(srcjardir)\doc2html.jar

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

	
# DEPENDENCIES
	
$(classfiles): $(javafiles)

$(srcjarfile): $(classfiles)

$(onejarout): $(srcjarfile)

# MAKE THE ONEJAR FILE
# JAR will not output to a specific directory, use 7z	
onejar: $(onejarout) $(srcjarfile) $(classfiles) 
	rmdir /S /Q $(rootdir)
	7z x -o$(rootdir) $(onejarfile)
	rmdir /S /Q $(rootdir)/src
	mkdir $(rootdir)\main
	mkdir $(rootdir)\lib
	mv $(srcjarfile) $(rootdir)\main
	mv $(scrjardir)\*.jar $(rootdir)\lib
	del /Q $(onejarout)
	cd $(rootdir)
	jar -cvmf $(onejarmanifest) $(onejarout) .
	mv $(rootdir)\main\doc2html.jar $(srcjardir)
	mv $(rootdir)\lib\*.jar $(srcjardir)
	
	

	
	
	
