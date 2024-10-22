SCRIPT=main.py
DATA=input_matrix.txt
BLOCK_SIZES='2 4 6 20'

python "$(dirname "$0")/${SCRIPT}" ${DATA} ${BLOCK_SIZES}
