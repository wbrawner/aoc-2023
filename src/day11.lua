require "util"

function parse_galaxies(input)
    local galaxies = {}
    local empty_rows = {}
    local empty_cols = {}
    for y, line in ipairs(input) do
        empty_rows[y] = true
        for x = 1, #line do
            if empty_cols[x] == nil then
                empty_cols[x] = true
            end
            if line:sub(x, x) == "#" then
                empty_rows[y] = false
                empty_cols[x] = false
                galaxies[#galaxies + 1] = { x = x, y = y }
            end
        end
    end
    return galaxies, empty_rows, empty_cols
end

function calculate_offsets(data, offset_increment)
    local offsets = {}
    local data_index = 1
    local offset = 0
    for i, empty in ipairs(data) do
        if empty then
            offset = offset + offset_increment - 1
        end
        offsets[i] = offset
    end
    return offsets
end

function expand_universe(galaxies, empty_rows, empty_cols, expansion_amount)
    local row_offsets = calculate_offsets(empty_rows, expansion_amount)
    local col_offsets = calculate_offsets(empty_cols, expansion_amount)
    for i, galaxy in ipairs(galaxies) do
        galaxies[i] = {
            x = galaxy.x + col_offsets[galaxy.x],
            y = galaxy.y + row_offsets[galaxy.y],
        }
    end
end

function distance(a, b)
    return math.abs(a.x - b.x) + math.abs(a.y - b.y)
end

function part1(input, expansion_amount)
    local galaxies, empty_rows, empty_cols = parse_galaxies(input)
    expand_universe(galaxies, empty_rows, empty_cols, expansion_amount)
    local distances = 0
    for i, galaxy in ipairs(galaxies) do
        for j = i + 1, #galaxies do
            local other_galaxy = galaxies[j]
            distances = distances + distance(galaxy, other_galaxy)
        end
    end
    return distances
end

local test_input = read_test_input()
assert_equals(part1(test_input, 2), 374)
assert_equals(part1(test_input, 10), 1030)
assert_equals(part1(test_input, 100), 8410)
local input = read_input()
print(part1(input, 2))
print(part1(input, 1000000))
