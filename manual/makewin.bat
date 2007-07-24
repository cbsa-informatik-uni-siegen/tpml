@echo off
pdflatex manual.tex
	
makeindex manual.idx -o manual.ind
	pdflatex manual.tex