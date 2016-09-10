const mongoose = require('mongoose');

const Schema = mongoose.Schema;

var caseSchema = new Schema({
  	patient:{
  		id: String,
  		name: String
  	},
  	doctor: {
  		id: String,
  		name: String,
  	},
  	provider: {
  		id: String,
  		name: String,	
  	},
  	status: String,
  	owner: String,
  	data: {
  		clinical: [{
  			id: String,
  			name: String,	
  			value: String
  		}],
  		test: [{
  			id: String,
  			name: String,	
  			value: String
  		}]
  	},
  	pendingdata: {
  		clinical: [{
  			id: String,
  			name: String
  		}],
  		test: [{
  			id: String,
  			name: String	
  		}]
  	},
  	prescription: {
  		date:String,
  		medicines: [ {
  			name: String,
  			intake: String,
  			duration: String
  		}],
  		notes: String,
  		review: String
  	}
  });
module.exports = mongoose.model('Cases', caseSchema);