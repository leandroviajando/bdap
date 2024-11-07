SHELL     := /usr/bin/env bash
MAKEFLAGS += --silent

BIN_DIR = ./bin
SRC_DIR = ./src

default: clean compile run

.PHONY: clean compile run

# Clean the compiled files
clean:
	$(RM) -rf $(BIN_DIR)

# Compile the source code
compile:
	mkdir -p $(BIN_DIR)
	javac -d $(BIN_DIR) $(SRC_DIR)/*.java

# Run the code in Main.java
run: compile
	java -cp $(BIN_DIR) Main
