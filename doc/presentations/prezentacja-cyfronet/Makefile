name=prezentacja-zebranie

all: $(name).pdf

$(name).pdf: *.tex
	pdflatex $(name)

clean:
	rm -rf $(name).pdf *.aux *.log *.toc *.dvi *.bbl *.out *.snm *.nav *.blg
