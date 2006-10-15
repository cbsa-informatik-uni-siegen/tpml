# $Id$

TEXFILES =								\
	TP.tex								\
	anhanga.tex							\
	kap1.tex							\
	kap2.tex							\
	kap3.tex							\
	kap4.tex							\
	manual.tex

all: pdf

pdf: manual.pdf

manual.pdf: $(TEXFILES)
	pdflatex manual.tex
	makeindex manual.idx -o manual.ind
	pdflatex manual.tex

clean:
	rm -f *.aux
	rm -f *.dvi
	rm -f *.log
	rm -f *.pdf
	rm -f *.ps
	rm -f *.toc
	rm -f *.idx
	rm -f *.ilg
	rm -f *.ind

# vi:set ts=8 sw=8 noet syntax=make:
