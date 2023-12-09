function readInput(file)
    local lines = {}
    for line in io.lines(file) do
        lines[#lines + 1] = line
    end
    return lines
end

function assert_equals(actual, expected, message)
    if actual ~= expected then
        print(string.format("assert failed: %s", message or ""))
        print(string.format("\texpected: %s", expected))
        print(string.format("\tactual: %s", actual))
    end
end
