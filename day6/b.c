#include <stdio.h>
#include <stdlib.h>

typedef struct {
	int x;
	int y;
	int area;
	int infinite;
} Position;

int main() {
	// Open file
	FILE *file = fopen("input.txt", "r");
	if(file == NULL) {
		printf("Failed to open file!");
		return 1;
	}

	// Setup
	char line[1024]; // Create horribly hard-coded buffers
	Position points[1024];
	int points_size = 0;
	int grid[1024][1024];
	int top, bottom, left, right;

	// Read file
	for(int i = 0, x = 0, y = 0; i < 1024 && fgets(line, sizeof line, file) != NULL; i += 1) {
		// Parse
		sscanf(line, "%d, %d", &x, &y); // Ewww

		// Log points
		points[i] = (Position){x, y};
		points_size = i + 1;

		// Update bounds
		if(i == 0 || x < left) left = x;
		if(i == 0 || x > right) right = x;
		if(i == 0 || y < top) top = y;
		if(i == 0 || y > bottom) bottom = y;
	}
	fclose(file);

	// Fill grid
	for(int y = top - 1; y <= bottom + 1; y++) {
		for(int x = left - 1; x <= right + 1; x++) {
			for(int i = 0; i < points_size; i++) {
				grid[y][x] += abs(x - points[i].x) + abs(y - points[i].y);
			}
		}
	}

	int total = 0;
	for(int y = top - 1; y <= bottom + 1; y++) {
		for(int x = left - 1; x <= right + 1; x++) {
			// printf(grid[y][x] < 10000 ? "#" : " ");
			if(grid[y][x] < 10000) total += 1;
		}
		// printf("\n");
	}

	// Print it
	printf("%d", total);

	return 0;
}
