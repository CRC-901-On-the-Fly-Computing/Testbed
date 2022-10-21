function createString(name){
  var toReturn = "\{\"name\":\""+name+"\" \}";
  return toReturn;
}
const execSync = require('child_process').execSync;
const fs = require('fs');

var reader = require('readline').createInterface({
  input: fs.createReadStream('classes')
});

fs.writeFileSync("out/module", "", 'utf-8');
execSync('rm out/*');

reader.on('line', function (line) {

  execSync('rm tmp/*');
  console.log('The current line is: ', line);

  fs.writeFileSync("tmp/config.json", createString(line), 'utf-8');

  execSync('mustache tmp/config.json templates/deserializer.mustache > out/'+ line+ 'Deserializer.java');
  execSync('mustache tmp/config.json templates/serializer.mustache > out/'+ line+ 'Serializer.java');
  execSync('mustache tmp/config.json templates/module.mustache > tmp/module');

  var tmpreader = require('readline').createInterface({
    input: fs.createReadStream('tmp/module')
  });
  tmpreader.on('line', function (myline) {
    fs.appendFileSync('out/module', myline+'\n', 'utf-8');
  });
  fs.appendFileSync('out/module', '\n', 'utf-8');
});
