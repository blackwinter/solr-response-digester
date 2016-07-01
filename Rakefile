require 'rake/clean'

chdir('src')

solr_dir = File.expand_path(File.join(ENV['SOLR_DIR'] || '.', 'solr'))

src_list = FileList['**/*.java']
cls_list = src_list.ext('class')
main_src = src_list.grep(/ResponseDigester/).first or abort 'Source not found!'

pkg_name = File.read(main_src)[/^package\s+(\S+)\s*;/, 1]
jar_file = File.join(solr_dir, "#{pkg_name}.jar")

CLEAN.add(jar_file)
CLOBBER.add(cls_list)

task default: :build

desc 'Compile source files'
task compile: cls_list

desc "Build #{jar_file}"
task build: jar_file

file jar_file => cls_list do
  sh 'jar', 'cvf', jar_file, *cls_list
end

rule '.class' => '.java' do |t|
  ver = ENV['JAVA_VER'] || ENV_JAVA['java.specification.version']

  cp = {
    'lucene-libs' => 'lucene-core',
    'solr-core'   => 'solr-core',
    'solr-solrj'  => 'solr-solrj'
  }.map { |key, val|
    glob = "#{solr_dir}/build/#{key}/#{val}-*-SNAPSHOT.jar"
    Dir[glob].first or abort "JAR not found: #{glob}" }.push('.')

  sh 'javac', '-source', ver, '-target', ver, '-cp', cp.join(':'), t.source
end
