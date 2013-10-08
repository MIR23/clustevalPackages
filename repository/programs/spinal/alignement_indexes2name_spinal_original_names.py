import sys
import itertools
import networkx as nx

class ConvertOriginal:
	def __init__(self, ppi1filename, ppi2filename, matchfilename, outmatchfilename):
		self.ppi1 = nx.read_gml(ppi1filename, relabel = True)
		self.ppi2 = nx.read_gml(ppi2filename, relabel = True)
		in_to_node1 = {}
		in_to_node2 = {}
		for n1 in self.ppi1.nodes():
			in_to_node1[self.ppi1.node[n1]['index']] = n1
		for n2 in self.ppi2.nodes():
			in_to_node2[self.ppi2.node[n2]['index']] = n2

		matchfile = open(matchfilename)
		outmatchfile = open(outmatchfilename, "w")
		self.I = []
		for line in matchfile:
			if line[0] == "!":
				outmatchfile.write(line)
				continue
			cols = line.split()
			outmatchfile.write(in_to_node1[int(cols[0])] + " ")
			outmatchfile.write(in_to_node2[int(cols[1])] + "\n")
			self.I.append([in_to_node1[int(cols[0])], in_to_node2[int(cols[1])]])

		#check if it's a legal match
		for (i, j) in itertools.product(self.I, self.I):
			if i!= j:
				if (i[0] == j[0]) or (i[1] == j[1]):
					print "not a legal match: ", (i,j)
					return
		print "Legal match" 


		matchfile.close()
		outmatchfile.close()

if __name__ == "__main__":
	"""sample run: python pyscripts/spinal_original_names.py data/dmela.tab.gml data/scere.tab.gml  
	   data/results.txt data/newresults.txt"""
	ConvertOriginal(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4])
			
			
																					
