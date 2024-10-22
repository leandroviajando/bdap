import collections
import pathlib
import statistics
import subprocess
import sys
from typing import Dict, Final, List, Literal, NamedTuple, Union, cast

import matplotlib.pyplot as plt
import numpy as np
import tabulate

ASSETSDIR: Final = (WORKDIR := pathlib.Path(__file__).parent) / "assets"
BINDIR: Final = WORKDIR / "bin"
DATADIR: Final = WORKDIR / "data"
EXECUTABLE_PATH: Final = BINDIR / "matrix_rotation.o"
FIGURE_PATH: Final = ASSETSDIR / "graph.png"

NUM_EXPERIMENTS: Final = 100

X_AXIS_LABEL: Final = "Matrix Size (n)"
Y_AXIS_LABEL: Final = "Time (seconds)"


class ExperimentResult(NamedTuple):
    avg: float
    stdev: float


BlockSize = int
MatrixSize = int
Strategy = Union[Literal["naive"], BlockSize]
ExperimentsResults = Dict[MatrixSize, Dict[Strategy, ExperimentResult]]


def call_subprocess(strategy: Strategy, n: MatrixSize, filename: str) -> list[str]:
    """Call the subprocess for matrix rotation and return the output as a list of strings."""

    return (
        subprocess.run(
            [
                EXECUTABLE_PATH,
                strategy if isinstance(strategy, str) else "blocked",
                str(n),
                filename,
                str(strategy),
            ],
            stdout=subprocess.PIPE,
            text=True,
        )
        .stdout.strip()
        .split("\n")
    )


def display_results(results: ExperimentsResults, num_experiments: int) -> None:
    """Display the results in a tabulated format."""

    table_data = []
    for n, data in results.items():
        for strategy, time in data.items():
            table_data.append([n, strategy, time.avg, time.stdev])

    print(f"{num_experiments = }")
    print(
        tabulate.tabulate(
            table_data,
            headers=[X_AXIS_LABEL, "Strategy", Y_AXIS_LABEL, "Standard deviation"],
            tablefmt="grid",
        )
    )


def plot_results(
    results: ExperimentsResults,
    num_experiments: int,
    *,
    savefig: Union[pathlib.Path, str] = FIGURE_PATH,
) -> None:
    """Plot the results of matrix rotation experiments."""

    n_values = sorted(results.keys())
    strategies = set(strategy for data in results.values() for strategy in data.keys())
    bar_width = 0.15
    x_indexes = np.arange(len(n_values))

    for i, strategy in enumerate(strategies):
        avg_time_values = [results[n].get(strategy, (0, 0))[0] for n in n_values]
        stdev_time_values = [results[n].get(strategy, (0, 0))[1] for n in n_values]
        plt.bar(
            x_indexes + i * bar_width,
            avg_time_values,
            width=bar_width,
            label=str(strategy),
            yerr=stdev_time_values,
        )

    plt.xlabel(X_AXIS_LABEL)
    plt.ylabel(Y_AXIS_LABEL)
    plt.title(
        f"Matrix Rotation Time Comparison (averaged over {num_experiments} experiments)"
    )
    plt.xticks(
        x_indexes + bar_width * (len(strategies) - 1) / 2, list(map(str, n_values))
    )
    plt.legend()
    plt.savefig(savefig)
    plt.show()


if __name__ == "__main__":
    file_name = sys.argv[1]
    block_sizes = list(map(lambda block: int(block), sys.argv[2:]))

    print(
        f"This script executes matrix rotations for the matrices in file '{file_name}'",
        f"using the naive strategy, and the blocked strategy for block sizes: {', '.join(sys.argv[2:])}.",
    )

    with open(DATADIR / file_name, "r") as file:
        matrix_info = [
            (int(num), filename)
            for line in file.readlines()
            for num, filename in [line.strip().split()]
        ]

    results: ExperimentsResults = collections.defaultdict(dict)
    strategies = cast(List[Strategy], ["naive"] + block_sizes)

    for n, filename in matrix_info:
        for strategy in strategies:
            if isinstance(strategy, str) or strategy <= n:
                times = [
                    float(call_subprocess(strategy, n, filename)[-1].split("=")[-1])
                    for _ in range(NUM_EXPERIMENTS)
                ]

                results[n][strategy] = ExperimentResult(
                    avg=statistics.mean(times), stdev=statistics.stdev(times)
                )

    display_results(results, num_experiments=NUM_EXPERIMENTS)
    plot_results(results, num_experiments=NUM_EXPERIMENTS)
