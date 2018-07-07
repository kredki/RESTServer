"use strict";
var serverURL = 'http://localhost:8000/oceny/';

Date.prototype.yyyymmdd = function () {
    var mm = (this.getMonth() + 1).toString(); // getMonth() is zero-based
    var dd = this.getDate().toString();

    return [this.getFullYear(), '-', mm.length === 2 ? '' : '0', mm, dd.length === 2 ? '-' : '0', dd].join(''); // padding
};

function ViewModel() {
    // Data
    var self = this;
    self.courses = ko.observableArray([]);
    self.students = ko.observableArray([]);
    self.grades = ko.observableArray([]);

    $.getJSON(serverURL + 'courses', function (allData) {
        ko.mapping.fromJS(allData, {}, self.courses);
    });

    $.getJSON(serverURL + 'students', function (allData) {
        ko.mapping.fromJS(allData, {}, self.students);
        console.log(students);
    });

    self.seeGrades = function (index) {
        console.log('row index ' + index);
        var x = self.students()[index];
        console.log('student index ' + x.index());

        window.location = "#grades";
        var studentIndex = x.index();
        localStorage.setItem('selectedStudentIndex', studentIndex);

        $.getJSON(serverURL + 'students/' + studentIndex + '/grades', function (allData) {
            ko.mapping.fromJS(allData, {}, self.grades);
            console.log(grades);
        });
    }

    self.newGradeValue = ko.observable();
    self.newGradeCourse = ko.observable();
    self.selectedCopurse = ko.observable();

    self.addGrade = function () {
        var selectedStudentIndex = localStorage.getItem('selectedStudentIndex');
        console.log("selected student index " + selectedStudentIndex);
        console.log("new grade value " + self.newGradeValue());
        console.log("new course " + self.selectedCopurse().objectId());
        var date = new Date();
        var dateStr = date.getFullYear() + '-' + date.getMonth() + '-' + date.getDate();

        var date = new Date();
        var todayString = date.yyyymmdd();
        console.log("new course date " + todayString);

        var gradeToAdd = {
            value: self.newGradeValue,
            date: todayString,
            course: {
                objectId: self.selectedCopurse().objectId()
            }
        }
        console.log("new grade " + gradeToAdd);

        $.ajax(serverURL + 'students/' + selectedStudentIndex + '/grades', {
            data: ko.toJSON(gradeToAdd),
            type: "post",
            contentType: "application/json",
            success: function (result) {
                alert("grade added");
            }
        });
    }

    self.deleteGrade = function (index) {
        var selectedStudentIndex = localStorage.getItem('selectedStudentIndex');
        console.log("selected student index " + selectedStudentIndex);

        console.log('row index ' + index);
        var gradeId = self.grades()[index].id();
        console.log("selected grade id " + gradeId);

        $.ajax(serverURL + 'students/' + selectedStudentIndex + '/grades/' + gradeId, {
            type: "delete",
            success: function (result) {
                alert("grade deleted");
                $.getJSON(serverURL + 'students/' + selectedStudentIndex + '/grades', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.grades);
                });
            }
        });
    }


    self.updateGrade = function (index) {
        var select = document.getElementById('courseSelect');
        // return the value of the selected option
        var courseId = select.options[select.selectedIndex].value;
        courseId = Number(courseId);
        console.log('courseId ' + courseId);
        
        var courseObjectId;
        var i;
        for (i = 0; i < self.courses().length; i++) {
            var id = self.courses()[i].id()
            console.log('ad' + id)
            if(self.courses()[i].id() === courseId) {
                courseObjectId = self.courses()[i].objectId();
                break;
            }
        }
        console.log('courseObjectId ' + courseObjectId);

        var selectedStudentIndex = localStorage.getItem('selectedStudentIndex');
        console.log("selected student index " + selectedStudentIndex);

        console.log('row index ' + index);
        var gradeToUpdate = self.grades()[index];
        console.log('gradeToUpdate ' + gradeToUpdate);
        var gradeId = gradeToUpdate.id();
        console.log('gradeId ' + gradeId);
        var gradeObjectId = gradeToUpdate.objectId();
        console.log('gradeObjectId ' + gradeObjectId);
        var gradeValue = gradeToUpdate.value();
        console.log('gradeValue ' + gradeValue);


        var gradeDate = gradeToUpdate.date();
        console.log('studentBirthday ' + gradeDate);

        var updateGradeJSON = {
            "id": gradeId,
            "value": gradeValue,
            "date": gradeDate,
            "objectId": gradeObjectId,
            course: {
                objectId: courseObjectId
            }
        }

        $.ajax(serverURL + 'students/' + selectedStudentIndex + '/grades/' + gradeId, {
            data: ko.toJSON(updateGradeJSON),
            type: "put",
            contentType: "application/json",
            success: function (result) {
                alert("grade updated");
                $.getJSON(serverURL + 'students/' + selectedStudentIndex + '/grades/', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.grades);
                });
            },
            error: function (xhr, textStatus, errorThrown) {
                alert('request failed');
                $.getJSON(serverURL + 'students/' + selectedStudentIndex + '/grades/', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.grades);
                });
            }
        });
    }

    self.newCourseName = ko.observable();
    self.newCourseLecturer = ko.observable();

    self.addCourse = function () {
        var courseToAdd = {
            name: self.newCourseName(),
            lecturer: self.newCourseLecturer()
        };
        console.log(courseToAdd);
        $.ajax(serverURL + 'courses', {
            data: ko.toJSON(courseToAdd),
            type: "post",
            contentType: "application/json",
            success: function (result) {
                alert("course added");
                $.getJSON(serverURL + 'courses', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.courses);
                });
            }
        });
    };

    self.deleteCourse = function (index) {
        console.log('row index ' + index);
        var x = self.courses()[index];
        console.log('course id ' + x.id());
        var courseId = x.id();

        $.ajax(serverURL + 'courses/' + courseId, {
            type: "delete",
            success: function (result) {
                alert("course deleted");
                $.getJSON(serverURL + 'courses', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.courses);
                });
            },
            error: function (xhr, textStatus, errorThrown) {
                console.log('error= ' + errorThrown);
                var msg = '';
                if (errorThrown == 'Not Acceptable') {
                    msg = 'course has grades';
                }
                alert('request failed ' + msg);
            }
        });
    }

    self.updateCourse = function (index) {
        console.log('row index ' + index);
        var courseToUpdate = self.courses()[index];
        console.log('course ' + courseToUpdate);
        var courseId = courseToUpdate.id();
        console.log('course id ' + courseId);
        var courseObjectId = courseToUpdate.objectId();
        console.log('courseObjectId ' + courseObjectId);
        var courseName = courseToUpdate.name();
        console.log('courseName ' + courseName);
        var courseLecturer = courseToUpdate.lecturer();
        console.log('courseObjectId ' + courseLecturer);

        var updateCourseJSON = {
            "id": courseId,
            "name": courseName,
            "lecturer": courseLecturer,
            "objectId": courseObjectId
        }

        $.ajax(serverURL + 'courses/' + courseId, {
            data: ko.toJSON(updateCourseJSON),
            type: "put",
            contentType: "application/json",
            success: function (result) {
                alert("course updated");
                $.getJSON(serverURL + 'courses', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.courses);
                });
            },
            error: function (xhr, textStatus, errorThrown) {
                alert('request failed');
                $.getJSON(serverURL + 'courses', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.courses);
                });
            }
        });
    }

    self.newStudentFirstName = ko.observable();
    self.newStudentLastName = ko.observable();
    self.newStudentBirthday = ko.observable();

    self.addStudent = function () {
        var studentToAdd = {
            firstName: self.newStudentFirstName(),
            lastName: self.newStudentLastName(),
            birthday: self.newStudentBirthday()
        };
        console.log(studentToAdd);
        $.ajax(serverURL + 'students', {
            data: ko.toJSON(studentToAdd),
            type: "post",
            contentType: "application/json",
            success: function (result) {
                alert("student added");
                $.getJSON(serverURL + 'students', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.students);
                });
            }
        });
    }

    self.deleteStudent = function (index) {
        console.log('row index ' + index);
        var x = self.students()[index];
        var studentIndex = x.index();
        console.log('student index ' + studentIndex);

        $.ajax(serverURL + 'students/' + studentIndex, {
            type: "delete",
            success: function (result) {
                alert("student deleted");
                $.getJSON(serverURL + 'students', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.students);
                });
            }
        });
    }

    self.updateStudent = function (index) {
        console.log('row index ' + index);
        var studentToUpdate = self.students()[index];
        console.log('studentToUpdate ' + studentToUpdate);
        var studentIndex = studentToUpdate.index();
        console.log('studentIndex ' + studentIndex);
        var studentObjectId = studentToUpdate.objectId();
        console.log('studentObjectId ' + studentObjectId);
        var studentLastName = studentToUpdate.lastName();
        console.log('studentLastName ' + studentLastName);
        var studentFrstName = studentToUpdate.firstName();
        console.log('studentFrstName ' + studentFrstName);
        var studentBirthday = studentToUpdate.birthday();
        console.log('studentBirthday ' + studentBirthday);

        var updateCourseJSON = {
            "index": studentIndex,
            "lastName": studentLastName,
            "firstName": studentFrstName,
            "birthday": studentBirthday,
            "objectId": studentObjectId
        }

        $.ajax(serverURL + 'students/' + studentIndex, {
            data: ko.toJSON(updateCourseJSON),
            type: "put",
            contentType: "application/json",
            success: function (result) {
                alert("student updated");
                $.getJSON(serverURL + 'students', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.students);
                });
            },
            error: function (xhr, textStatus, errorThrown) {
                alert('request failed');
                $.getJSON(serverURL + 'students', function (allData) {
                    ko.mapping.fromJS(allData, {}, self.students);
                });
            }
        });
    }
    
    self.searchStudentFirstName = ko.observable();
    self.searchStudentLastName = ko.observable();
    self.searchStudentBirthday = ko.observable();
    
    self.searchStudent = function () {
        var firstName = self.searchStudentFirstName();
        var lastName = self.searchStudentLastName();
        var birthday = self.searchStudentBirthday();
        
        if(firstName === undefined) {
            firstName = '';
        }
        if(lastName === undefined) {
            lastName = '';
        }
        if(birthday === undefined) {
            birthday = '';
        }
        
        console.log('searchStudentFirstName = ' + firstName);
        console.log('searchStudentLastName = ' + lastName);
        console.log('searchStudentBirthday = ' + birthday);
        
        $.getJSON(serverURL + 'students?firstname=' + firstName + '&lastname=' + lastName + '&birthday=' + birthday
                  , function (allData) {
        ko.mapping.fromJS(allData, {}, self.students);
        console.log(students);
    });
    }
    
    self.searchCourseLecturer = ko.observable();
    
    self.searchCourse = function () {
        var lecturer = self.searchCourseLecturer();
        
        if(lecturer === undefined) {
            lecturer = '';
        }
        
        console.log('searchCourseName = ' + name);
        console.log('searchCourseLecturer = ' + lecturer);
        
        $.getJSON(serverURL + 'courses?lecturer=' + lecturer
                  , function (allData) {
        ko.mapping.fromJS(allData, {}, self.courses);
    });
    }
    
    self.searchGradesValue = ko.observable();
    self.searchGradesCourseId = ko.observable();
    
    self.searchGrade = function () {
        var selectedStudentIndex = localStorage.getItem('selectedStudentIndex');
        console.log('selectedStudentIndex = ' + selectedStudentIndex);
        
        var select = document.getElementById('gradeCourseSelect');
        // return the value of the selected option
        var courseId = select.options[select.selectedIndex].value;
        courseId = Number(courseId);
        console.log('courseId ' + courseId);
        
        var select = document.getElementById('gradeValueSelect');
        // return the value of the selected option
        var gradeValue = select.options[select.selectedIndex].value;
        console.log('gradeValue ' + gradeValue);
        
        $.getJSON(serverURL + 'students/' + selectedStudentIndex + '/grades?gradegreater=' + gradeValue + '&course=' + courseId
                  , function (allData) {
            ko.mapping.fromJS(allData, {}, self.grades);
            console.log(grades);
        });
    }
}

ko.applyBindings(new ViewModel());
