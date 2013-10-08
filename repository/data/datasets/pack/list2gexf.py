import sys
import networkx as nx

class GenerateGEFX:
	def __init__(self, ppi1filename, outppi1filename):
		
		ppi1file = open(ppi1filename)
		self.ppi1 = nx.Graph()
			
		for i, gt in enumerate(([self.ppi1, ppi1file], [self.ppi1, ppi1file])):
			#gt[1].readline()
			for line in gt[1]:
				cols = line.split('\t')
				gt[0].add_edge(cols[0], cols[1])
			for j, protein in enumerate(gt[0].nodes()):
				gt[0].node[protein]['index'] = j
			break	
		
		#self.ppi1.remove_edges_from(self.ppi1.selfloop_edges()) 
		
		for n in self.ppi1.nodes():
		    self.ppi1.node[n]['gname'] = n
		nx.write_gexf(self.ppi1, outppi1filename)
		ppi1file.close()

if __name__ == "__main__":
	inputdata = GenerateGEFX(sys.argv[1], sys.argv[2])
    