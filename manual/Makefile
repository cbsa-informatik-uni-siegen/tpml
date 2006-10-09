# $Id$

TEXFILES =								\
	TP.tex								\
	kap1.tex							\
	kap2.tex							\
	kap3.tex							\
	manual.tex

all: pdf

pdf: manual.pdf

manual.pdf: $(TEXFILES)
	pdflatex manual.tex
	pdflatex manual.tex

clean:
	rm -f *.aux
	rm -f *.dvi
	rm -f *.log
	rm -f *.pdf
	rm -f *.ps
	rm -f *.toc

# vi:set ts=8 sw=8 noet syntax=make:
