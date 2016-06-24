require 'rake/clean'

src = File.expand_path(ENV['SRC'] || 'src')
dir = File.expand_path(File.join(ENV['DIR'] || '.', 'solr'))

srz = Dir["#{src}/**/*.java"].first or abort "SRC not found!"

cls = File.basename(srz, File.extname(srz))
pkg = File.read(srz)[/^package\s+(\S+)\s*;/, 1]

CLEAN   << jar = File.join(dir, "#{pkg}.jar")
CLOBBER << clz = srz.sub('.java', '.class')

task default: :build

desc "Compile #{srz}"
task compile: clz

desc "Build #{jar}"
task build: jar do
  puts <<-EOT
#{sep = '*' * 72}
#{dir}/example/techproducts/solr/techproducts/conf/solrconfig.xml:

<config>
  [...]
  <lib path="${solr.install.dir:../../../..}/#{File.basename(jar)}" />
  <queryResponseWriter name="xml" class="#{pkg}.#{cls}">
    <str name="key">KEY</str>
  </queryResponseWriter>
</config>
#{sep}
  EOT
end

file jar => clz do
  chdir(src) { sh 'jar', 'cvf', jar, clz.sub(src + '/', '') }
end

file clz => srz do
  ver = ENV['VER'] || ENV_JAVA['java.specification.version']

  jars = {
    'lucene-libs' => 'lucene-core',
    'solr-core'   => 'solr-core',
    'solr-solrj'  => 'solr-solrj'
  }.map { |key, val|
    glob = "#{dir}/build/#{key}/#{val}-*-SNAPSHOT.jar"
    Dir[glob].first or abort "JAR not found: #{glob}" }.join(':')

  sh 'javac', '-source', ver, '-target', ver, '-cp', jars, srz
end
