name=dri-description

all: $(name).pdf

$(name).pdf: *.tex
	pdflatex $(name)
	pdflatex $(name)

clean:
	rm -rf $(name).pdf *.aux *.log *.toc *.dvi *.bbl *.out *.snm *.nav *.blg
