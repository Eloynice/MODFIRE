all: make

make:
	rm -rf *.txt
	python3 initialize_ugs.py
	python3 initialize_areas.py
	python3 initialize_years_fixed.py
	python3 initialize_cond.py
	python3 initialize_cond_presc.py
	python3 initialize_external.py
	python3 initialize_regions.py
	python3 initialize_xy.py
	python3 initialize_adj_ext.py

clean:
	rm -rf *.txt

