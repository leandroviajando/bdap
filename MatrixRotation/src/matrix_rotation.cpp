#include <cstring>
#include <ctime>
#include <filesystem>
#include <fstream>
#include <iostream>

const std::filesystem::path WORKDIR = std::filesystem::current_path();
const std::filesystem::path DATADIR = WORKDIR / "data";

/**
 * Rotate a square matrix in a naive way.
 *
 * This function rotates a square matrix in a naive way by swapping the elements
 * based on their indices. The rotation is performed in-place, meaning the
 * original matrix is modified.
 *
 * @param src   Pointer to the input matrix.
 * @param dest  Pointer to the output matrix.
 * @param n     The size of the square matrix.
 */
void rotate_naive(double *src, double *dest, int n) {
  for (int i = 0; i < n; i++)
    for (int j = 0; j < n; j++) dest[j * n + i] = src[(n - 1 - i) * n + j];
}

/**
 * Rotate a square matrix in blocked fashion.
 *
 * This function rotates a square matrix by 90 degrees clockwise in a blocked
 * fashion, where each block of size B x B is rotated individually. The input
 * matrix 'src' is rotated and the result is stored in the output matrix 'dest'.
 *
 * Note: The difference to the naive rotation is: `bi`, `bj` instead of `i`,
 * `j`:
 *
 * `dest[j * n + i] = src[(n - 1 - i) * n + j]`
 *
 * `dest[bj * n + bi] = src[(n - 1 - bi) * n + bj]`
 *
 * For larger matrices (n>1000) this is more efficient because it reduces cash
 * misses significantly.
 *
 * @param src   Pointer to the input matrix.
 * @param dest  Pointer to the output matrix.
 * @param n     Size of the square matrix.
 * @param B     Size of the block.
 */
void rotate_blocked(double *src, double *dest, int n, int B) {
  for (int i = 0; i < n; i += B)
    for (int j = 0; j < n; j += B)
      for (int bi = i; bi < std::min(i + B, n); bi++)
        for (int bj = j; bj < std::min(j + B, n); bj++)
          dest[bj * n + bi] = src[(n - 1 - bi) * n + bj];
}

/**
 * Validate that blocked and naive rotations give the same result.
 *
 * @param src   Pointer to the input matrix.
 * @param dest  Pointer to the output matrix.
 * @param n     Size of the square matrix.
 * @param B     Size of the block.
 */
void validate_results(double *src, double *dest, int n, int B) {
  double *naive_dest = new double[n * n];
  rotate_naive(src, naive_dest, n);

  double *blocked_dest = new double[n * n];
  rotate_blocked(src, blocked_dest, n, B);

  for (int i = 0; i < n * n; i++) {
    if (std::abs(naive_dest[i] - blocked_dest[i]) > 1e-10) {
      std::cerr << "Error: Blocked rotation produced different results than "
                   "naive rotation"
                << std::endl;
      free(naive_dest);
      free(blocked_dest);
      free(src);
      free(dest);
      exit(1);
    }
  }

  free(naive_dest);
  free(blocked_dest);
}

/**
 * Reads a matrix from a file.
 *
 * @param filename The name of the file to read the matrix from.
 * @param matrix   A pointer to the array where the matrix will be stored.
 * @param n        The size of the matrix (n x n).
 */
void readMatrix(std::string filename, double *matrix, int n) {
  std::ifstream file(DATADIR / filename);
  if (file.is_open()) {
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++) {
        file >> matrix[i * n + j];
      }
    file.close();
  } else
    std::cout << "Unable to open file: " << DATADIR / filename << std::endl;
}

/**
 * Example usage:
 *
 * `./bin/matrix_rotation.o blocked 3 m3by3.txt 2`
 */
int main(int argc, char **argv) {
  std::string method = argv[1];
  int n = std::atoi(argv[2]);
  std::string filename = argv[3];
  int block_size = n;

  if (method == "blocked") block_size = std::atoi(argv[4]);

  std::cout << "method=" << method << std::endl;
  std::cout << "N=" << n << std::endl;
  std::cout << "B=" << block_size << std::endl;
  std::cout << "matrix=" << filename << std::endl;

  double *src = new double[n * n];
  double *dest = new double[n * n];

  readMatrix(filename, src, n);

  if (method == "blocked") validate_results(src, dest, n, block_size);

  clock_t begin, end;
  double time_spent;

  begin = clock();

  if (method == "blocked")
    rotate_blocked(src, dest, n, block_size);
  else
    rotate_naive(src, dest, n);

  end = clock();

  time_spent = static_cast<double>(end - begin) / CLOCKS_PER_SEC;
  std::cout << "time=" << time_spent << std::endl;

  free(src);
  free(dest);

  return 0;
}
