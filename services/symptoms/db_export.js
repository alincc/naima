var mongoose = require('mongoose');
var fs = require('fs');
var db = mongoose.connection;



db.on('error', console.error);
db.once('open', function() {
  var Symptoms = require('./model/Symptoms.js');
  var Cases = require('./model/Cases.js');
  var Conditions = require('./model/Conditions.js');
  var Patients = require('./model/Patients.js');
  var Doctors = require('./model/Doctors.js');
  var Rules = require('./model/AssociationRules.js');

  Conditions.find({}, function(err, condition) {
    if (err)
      console.log(JSON.stringify({}));
    else {
      var data = exportSymtomsList(condition);
      fs.writeFile("db.json", data, function(err) {
      if(err) {
          return console.log(err);
      }
      console.log("The file was saved!");
})
    }
  });

});

mongoose.connect('mongodb://localhost/test');

function exportSymtomsList (conditions) {
  var all = []
  for (i = 0; i < conditions.length; i++) { 
      var symptomIds = []
      symptomIds.push(conditions[i].id);
      var clinicalList = conditions[i].data.clinical;
      for (j = 0; j < clinicalList.length; j++) { 
           symptomIds.push(clinicalList[j].id);
      } 
      all.push(symptomIds.join())
  }
  return all.join('\n');
}
