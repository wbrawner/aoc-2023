---@diagnostic disable: lowercase-global
require "util"

function parse_node(line)
    local matches = {}
    for match in line:gmatch("%w+") do
        matches[#matches + 1] = match
    end
    return matches[1], { L = matches[2], R = matches[3] }
end

function values(src)
    local dest = {}
    for _,v in pairs(src) do
        dest[#dest + 1] = v
    end
    return dest
end

function part1(input)
    local steps = input[1]
    local nodes = {}
    for i = 3, #input do
        local node, children = parse_node(input[i])
        nodes[node] = children
    end
    local current_node = "AAA"
    local step_index = 1
    local step_count = 0
    while current_node ~= "ZZZ" do
        local nextStep = steps:sub(step_index, step_index)
        current_node = nodes[current_node][nextStep]
        step_count = step_count + 1
        step_index = step_index + 1
        if step_index > #steps then
            step_index = 1
        end
    end
    return step_count
end

function find_lcm(first, second)
    local max = math.max(first, second)
    local fcm = first * second
    local cm = max
    io.write(string.format("finding lcm of %s and %s... ", first, second))
    while cm <= fcm do
        if (cm % first == 0 and cm % second == 0) then
            print(cm)
            return cm
        end
        cm = cm + max
    end
end

function part2(input)
    local steps = input[1]
    local nodes = {}
    local current_nodes = {}
    for i = 3, #input do
        local node, children = parse_node(input[i])
        nodes[node] = children
        if string.sub(node, #node, #node) == "A" then
            current_nodes[node] = 0
        end
    end
    for node, _ in pairs(current_nodes) do
        local step_index = 1
        local current_node = node
        while current_node:sub(#current_node, #current_node) ~= "Z" do
            local nextStep = steps:sub(step_index, step_index)
            current_node = nodes[current_node][nextStep]
            current_nodes[node] = current_nodes[node] + 1
            step_index = step_index + 1
            if step_index > #steps then
                step_index = 1
            end
        end
    end
    local node_steps = values(current_nodes)
    local lcm = find_lcm(node_steps[1], node_steps[2])
    for i = 3, #node_steps do
        lcm=find_lcm(lcm, node_steps[i])
    end
    return lcm
end

local test_input = readInput("day08_test.txt")
local input = readInput("day08.txt")
print(part1(input))
assert_equals(part2(test_input), 6)
print(part2(input))
