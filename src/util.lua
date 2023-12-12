function get_day_name()
    return arg[0]:sub(1,5)
end

function read_input()
    local day = get_day_name()
    return read_file(day .. ".txt")
end

function read_test_input()
    local day = get_day_name()
    return read_file(day .. "_test.txt")
end

function read_file(file)
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
        os.exit(1)
    end
end
