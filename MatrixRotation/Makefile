SRCDIR=src
BINDIR=bin

CXX=g++ -std=c++17
CXXFLAGS=-o

default: $(BINDIR)/matrix_rotation.o

$(BINDIR)/matrix_rotation.o: $(SRCDIR)/matrix_rotation.cpp
	mkdir -p $(BINDIR)
	$(CXX) $< $(CXXFLAGS) $@

.PHONY: clean
clean:
	rm -r $(BINDIR)
