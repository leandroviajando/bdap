SCRIPT=main.py
DATA=input_matrix.txt
BLOCK_SIZES='2 4 8 16 32 64 128'

python "$(dirname "$0")/${SCRIPT}" ${DATA} ${BLOCK_SIZES}
