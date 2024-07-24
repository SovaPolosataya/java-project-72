.DEFAULT_GOAL := build-run

run-dist:
	make -C app run-dist

start:
	make -C app start

build:
	make -C app build

clean:
	make -C app clean

test:
	make -C app test

report:
	make -C app report

lint:
	make -C app lint

.PHONY: build