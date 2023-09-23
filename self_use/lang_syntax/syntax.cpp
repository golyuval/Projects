#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <set>

int main() {

    // ------------------------ Useful pointer/ref/value Syntax --------------------------------------------------------------------------

    int x = 8;  // Initialize x, ref: 0x01  [value: 8]
    int y = 9;  // Initialize y, ref: 0x02  [value: 9]

    int* p = &x;  // Pointer to x, ref: 0x01 [value: 8]
    // int* p = x; // Error: Cannot assign int to int pointer

    int& r = x;   // Reference to x, ref: 0x01 [value: 8]
    // int& r = &x; // Error: Cannot bind a reference to an address

    // int v = &x;  // Error: int cannot hold an address
    int v = x;    // [value: 8]

    // Changing the value of x through pointer p
    *p = 12;      // x is now 12, ref: 0x01 [value: 12]

    // Changing the value of x through reference r
    r = 15;       // x is now 15, ref: 0x01 [value: 15]


    // ------------------------ Useful string Syntax --------------------------------------------------------------------------

    int length = str.length(); // or str.size()
    int capacity = str.capacity();
    char firstChar = str[0];
    char lastChar = str.back();
    int intValue = std::stoi("123");
    double doubleValue = std::stod("3.14");
    const char* cstr = str.c_str(); // Conversion to C-style string

    str.substr(7, 3); // Starting from index 7, length 3
    str.append(" hi ");
    str.find("C++"); // Returns the index of the found substring
    str.replace(7, 3, "Python"); // Replace substring at index 7 with "Python"
    str.erase(0, 7); // Erase the first 7 characters
    std::transform(str.begin(), str.end(), str.begin(), ::toupper);
    std::transform(str.begin(), str.end(), str.begin(), ::tolower);
    str.erase(std::remove_if(str.begin(), str.end(), ::isspace), str.end()); // trim spaces

    return 0;
}



    // ------------------------ Useful std Syntax ----------------------------------------------------------------------


    double num = 3.7;

    std::floor(num);
    std::round(num);
    std::(num);
    std::pow(2, 3);  // 8
    std::sqrt(25);   // 5
    std::abs(-7);    // 7

    // Algorithm Functions
    std::vector<int> numbers = {5, 2, 8, 1, 9};
    *std::min_element(numbers.begin(), numbers.end()); // Find minimum element (returns 1)
    *std::max_element(numbers.begin(), numbers.end()); // Find maximum element (returns 9)
    std::is_sorted(numbers.begin(), numbers.end()); // Check if the range is sorted
    std::sort(numbers.begin(), numbers.end()); // sort numbers by IntroSort


    // ------------------------ Vector Useful Functions -------------- front ---> [0, 1, 2, 3] <--- back ---------------

    int element1 = 1;
    int element2 = 2;
    std::vector<int> vec = {5, 2, 8, 1, 9}; // [5 , 2 , 8 , 1 , 9]
    std::vector<int> stack_vec(5, 10); // [10, 10, 10, 10, 10]

    // Size / Capacity
    stack_vec.size() // vector size : 5
    stack_vec.capacity() // vector capacity : 5
    stack_vec.resize(10) // resizes vector to 10 with default values
    stack_vec.clear() // [], capacity remains the same
    stack_vec.empty() // true if empty


    // Inner Elements Access
    stack_vec.insert(stack_vec.begin(),element1); // front ----> [1 , 10, 10, 10, 10, 10]
    stack_vec.erase(stack_vec.begin()); // front ----> [10, 10, 10, 10, 10]
    stack_vec.push_back(element2); // [10, 10, 10, 10, 10, 2] <--- back       | emplace_back(element2) more efficient
    stack_vec.pop_back(); // [10, 10, 10, 10, 10] <--- back
    stack_vec.swap(vec) // swap vector's content :  vec --> [10,10,10,10,10]  ,  stack_vec --> [5,2,8,1,9]
    stack_vec.swap(vec) // swap back to normal :  stack_vec --> [10,10,10,10,10]  ,  vec --> [5,2,8,1,9]
    stack_vec.assign(vec.begin(), vec.end()); // stack_vec --> [5,2,8,1,9]  ,  vec --> [5,2,8,1,9]
    stack_vec.front(); // 5
    stack_vec.back();  // 9
    stack_vec.at(2);   // 8
    int* dataPtr = vec.data(); // Points to the underlying array

    // Iterators
    stack_vec.begin() // beginning of vector's position
    stack_vec.end() // end of vector position
    std::sort(vec.begin(), vec.end()); // Sorting the vector in ascending order
    std::reverse(vec.begin(), vec.end()); // Reversing the order of elements in the vector
    std::find(vec.begin(), vec.end(), element); // Finding an element in the vector



    // ------------------------ Map Useful Functions -------------------------------------------------------------------

    std::map<std::string, int> scores;

    // elements
    scores.insert(std::make_pair("Alice", 95));
    scores.emplace("Bob", 85);
    scores["Carol"] = 90;
    scores.erase(scores.begin()); // Erases element at the specified iterator
    scores.erase("Bob"); // Erases element with the given key

    // map
    std::map<std::string, int> temp;
    scores.swap(temp); // Swaps the contents of two maps
    scores.clear(); // Removes all elements from the map
    scores.count("Alice"); // Returns 1 if the key exists, 0 otherwise

    // Key / Value Comparison
    scores.key_comp(); // Returns the key comparison function
    scores.value_comp(); // Returns the value comparison function

    // Iterators
    scores.begin(); // Iterator to the beginning of the map
    scores.end(); // Iterator to the end of the map
    scores.rbegin(); // Reverse iterator to the last element
    scores.rend(); // Reverse iterator to one before the first element
    auto lower = scores.lower_bound("Carol"); // Iterator to the first element not less than "Carol"
    auto upper = scores.upper_bound("Eve"); // Iterator to the first element greater than "Eve"
    auto range = scores.equal_range("Bob"); // Pair of iterators representing the range of elements with key "Bob"



    // ------------------------ Set Useful Functions -------------------------------------------------------------------

    std::set<int> mySet;

    // Inserting elements into the set
    mySet.insert(5);
    mySet.insert(3);
    mySet.insert(8);
    int elem = 3;

    std::bool exists = mySet.find(elem) != mySet.end()// Checking if an element exists in the set


}
