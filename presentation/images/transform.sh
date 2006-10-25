#!/bin/sh

for figfile in *.fig; do
	basename=`echo $figfile | sed -e 's/\.fig$//'`
	echo $basename

	fig2dev -L pstex_t -p "TRANSFORM$basename.pstex" $figfile > "TRANSFORM$basename.pstex_t" || exit 1
	fig2dev -L pstex -p dummy $figfile > "TRANSFORM$basename.pstex" || exit 1

	texfile="TRANSFORM$basename.tex"
	cat > $texfile <<EOF
\\documentclass{article}
\\usepackage{epsfig}
\\usepackage{color}
\\setlength{\\textwidth}{100cm}
\\setlength{\\textheight}{100cm}
\\begin{document}
\\pagestyle{empty}
\\input{TRANSFORM$basename.pstex_t}
\\end{document}
EOF

	latex $texfile || exit 1
	dvips -E "TRANSFORM$basename.dvi" -o "TRANSFORM$basename.eps" || exit 1

	epstopdf "TRANSFORM$basename.eps" || exit 1

	mv -f "TRANSFORM$basename.pdf" "$basename.pdf" || exit 1

	rm -f TRANSFORM*
done
