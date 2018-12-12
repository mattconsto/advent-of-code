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
			grid[y][x] = -1;
			int best_distance = 9999998; // Really big, hard-coded
			int last_distance = 9999999; // A crude queue

			for(int i = 0; i < points_size; i++) {
				int current_distance = abs(x - points[i].x) + abs(y - points[i].y);

				if(current_distance <= best_distance) {
					last_distance = best_distance;
					best_distance = current_distance;
					grid[y][x] = i;
				}
			}

			// If two tie
			if(best_distance == last_distance) grid[y][x] = -2;
		}
	}

	// Fill grid, and calculate area
	char* output_map = ". abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	for(int y = top - 1; y <= bottom + 1; y++) {
		for(int x = left - 1; x <= right + 1; x++) {
			printf("%c", output_map[grid[y][x] + 2]);
			points[grid[y][x]].area += 1; // Count area

			if(y == top - 1 || y == bottom + 1 || x == left -1 || x == right + 1) {
				points[grid[y][x]].infinite = 1;
			}
		}
		printf("\n");
	}

	// Find largest area
	int largest_area = -1;
	for(int i = 0; i < points_size; i++) {
		if(!points[i].infinite && (largest_area == -1 || points[i].area > points[largest_area].area)) {
			largest_area = i;
		}
	}

	// Print it
	printf("%i", largest_area >= 0 ? points[largest_area].area : 0);

	return 0;
}
