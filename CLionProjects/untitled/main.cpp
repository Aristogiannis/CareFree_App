#include <iostream>
#include <string>
using namespace std;

class Student {
private:
    char* AM; // private attribute για τον Αριθμό μητρώου
    string fullName; // private attribute για το ονοματεπόνυμο
    unsigned int currentSemester; // private attribute για το εξάμηνο

public:
    // --- Κατασκευαστές / Constructors ---

    // Κατασκευαστής για την αρχηκοποίηση του currentSemester = 1 και για ορισμό του αριθμού μητρώου και ονοματεπόνυμου
    Student(char* am, string name) {
        AM = am;
        fullName = name;
        currentSemester = 1;
    }

    // Κατασκευαστής για τον ορισμό όλων των μεταβλητών (ΑΜ, ονοματεπόνυμω, εξάμηνο)
    Student(char* am, string name, unsigned int semester) {
        AM = am;
        fullName = name;
        currentSemester = semester;
    }

    // Copy constructor / Αντιγραφέας κατασκευαστής για την δημιουργία ενός νέου αντικείμενου με τις ίδιες τιμές χαρακτηριστικών με ένα άλλο αντικείμενο
    Student(const Student& other) {
        AM = other.AM;
        fullName = other.fullName;
        currentSemester = other.currentSemester;
    }

    // --- Getters ---

    char* getAM() const;

    string getFullName() const;

    unsigned int getCurrentSemester() const;

    // --- Setters ---

    void setAM(char* am);

    void setFullName(string name);

    void setCurrentSemester(unsigned int semester);

    // Διαδικασία print για εμφάνιση όλων των χαρακτηριστικών σε ενα output stream
    void print(ostream& os) const {
        os << "AM: " << AM << ", Full Name: " << fullName << ", Current Semester: " << currentSemester << endl;
    }

    // Τελεστές αύξησης για αύξηση του τρέχοντος εξαμήνου κατά 1
    Student& operator ++() { // Preincrement / προαύξηση, Το "&" στον τύπο Student& σε συνδυασμό με το "return *this" υποδεικνύει ότι αυτή η συνάρτηση υπερφόρτωσης τελεστή επιστρέφει μια αναφορά στο ίδιο το τροποποιημένο αντικείμενο Student.
        currentSemester++;
        return *this;
    }

    Student operator ++(int) { // Postincrement / μετααύξηση, Στη συνάρτηση υπερφόρτωσης τελεστή μετά την αύξηση, το σύμβολο "&" δε χρησιμοποιείται επειδή η συνάρτηση δεν επιστρέφει αναφορά στο αντικείμενο αντιθέτως, επιστρέφει ένα αντίγραφο του αντικειμένου πριν πραγματοποιηθεί η αύξηση.
        Student temp(*this);
        operator++();
        return temp;
    }

    // Τελεστές τροποποίησης εξαμήνου για αλλαγή του τρέχοντος εξαμήνου κατά ένα καθορισμένο ποσό
    //Το return *this χρησιμοποιείται για πρόσβαση στο τρέχον αντικείμενο, το οποίο τροποποιείται από τον τελεστή αύξησης. Επιστρέφοντας το *this, επιτρέπετε στο τροποποιημένο αντικείμενο να χρησιμοποιηθεί σε μια επόμενη έκφραση ή ανάθεση.
    //Κατά τη χρήση του return *this είναι σημαντικό να έχουμε ορίσει τύπο& και όχι τύπο, όπως ακριβώς φαίνεται στην συνέχεια (Student&). Διότι εάν ο τύπος επιστροφής δεν είναι αναφορά,η C++ θα δημιουργήσει ένα αντίγραφο του αντικειμένου και στη συνέχεια θα το καταστρέψει αμέσως, κάτι που δεν είναι η προβλεπόμενη συμπεριφορά.
    Student& operator +=(const int& semesters) {
        currentSemester += semesters;
        return *this;
    }

    Student& operator -=(unsigned int semesters) {
        currentSemester -= semesters;
        return *this;
    }
};

// Non-inline Getters
char* Student::getAM() const {
    return AM;
}

string Student::getFullName() const {
    return fullName;
}

unsigned int Student::getCurrentSemester() const {
    return currentSemester;
}

// Non-inline Setters
void Student::setAM(char* am) {
    AM = am;
}

void Student::setFullName(string name) {
    fullName = name;
}

void Student::setCurrentSemester(unsigned int semester) {
    currentSemester = semester;
}

int main() {
    // Ψευδής Δεδομένα για τη δοκιμή την κλάσης Student
    char ams[5][25] = {"1231", "2342", "12341", "21313", "432112"};
    string names [5] = {"Nikos", "Giorgos", "Eleni", "Marios", "Eleana"};
    unsigned int semesters [5] = {2,1,3,8,5};

    // 1: ορισμός του αριθμού μητρώου και ονοματεπόνυμου
    Student student1 = Student(ams[0], names[0]);
    student1.print(cout);

    // 2: Ορισμός σε όλα τα χαρακτηριστικά
    Student student2 = Student(ams[1], names[1], semesters[1]);
    student2.print(cout);

    // 3: Ορισμός όλων των χαρακτηριστικών στις τιμές χαρακτηριστικών ενός άλλου "Μαθητή".
    Student student3 = Student(student1);
    student3.print(cout);

    // 4: προαύξηση
    ++student1;
    student1.print(cout);

    // 5: μετααύξηση
    student2++;
    student2.print(cout);

    // 6: Αλλαγή του τρέχοντος εξαμήνου του φοιτητή1 χρησιμοποιώντας τους τελεστές += και -=
    student1 += 6;
    student2 -= 2;
    student1.print(cout);
    student2.print(cout);

    //Getters Testing
    cout << student1.getCurrentSemester() << endl;
    cout << student1.getAM()<< endl;
    cout << student1.getFullName() << endl;

    //Setters Testing
    student1.setCurrentSemester(12);
    student1.setAM("999");
    student1.setFullName("Ioannis");

    cout << student1.getCurrentSemester() << endl;
    cout << student1.getAM()<< endl;
    cout << student1.getFullName() << endl;

    return 0;
}

