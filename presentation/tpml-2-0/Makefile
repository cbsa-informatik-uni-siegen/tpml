# $Id: Makefile 25 2006-07-06 22:12:17Z benny $

TEXFILES =								\
	tp.tex								\
	einleitung.tex						\
	outline.tex							\
	callbyname.tex						\
	parser.tex							\
	sourcecode.tex						\
	objekte.tex							\
	klassen.tex							\
	latex.tex							\
	beweise.tex							\
	typeinference.tex					\
	minimaltyping.tex					\
	subtyping.tex						\
	fazit.tex							\
	verfeinerungen.tex					\
	view.tex							\
	vortrag.tex

all: pdf

pdf: vortrag.pdf

vortrag.pdf: $(TEXFILES)
	pdflatex vortrag.tex

clean:
	rm -f *.aux
	rm -f *.dvi
	rm -f *.log
	#rm -f *.pdf
	rm -f *.ps
	rm -f *.toc

# vi:set ts=8 sw=8 noet syntax=make:
