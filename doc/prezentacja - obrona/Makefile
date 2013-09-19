name=prezentacja

all: $(name).pdf

$(name).pdf: *.tex *.bib *.png
	pdflatex $(name)

clean:
	rm -rf $(name).pdf *.aux *.log *.toc *.dvi *.bbl *.out *.snm *.nav *.blg
