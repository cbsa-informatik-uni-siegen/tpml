# $Id: Makefile 1014 2007-01-11 13:32:11Z christian $

TEXFILES =								\
	ezs.tex

all: pdf

pdf: ezs.pdf

ezs.pdf: $(TEXFILES)
	pdflatex ezs.tex
	makeindex ezs.idx -o ezs.ind
	pdflatex ezs.tex

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
	rm -f *.out

# vi:set ts=8 sw=8 noet syntax=make:
