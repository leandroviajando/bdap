SRCDIR=src
BINDIR=bin

CXX=g++ -std=c++17
CXXFLAGS=-O3 -march=native -ffast-math -funroll-loops -o

default: clean $(BINDIR)/matrix_rotation.o run

$(BINDIR)/matrix_rotation.o: $(SRCDIR)/matrix_rotation.cpp
	mkdir -p $(BINDIR)
	$(CXX) $(CXXFLAGS) $@ $<

.PHONY: clean
clean:
	rm -r $(BINDIR)

.PHONY: run
run:
	./exe.sh
