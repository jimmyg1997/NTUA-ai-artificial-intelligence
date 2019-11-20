import java.io.IOException;
import java.util.ArrayList;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPSyntaxErrorException;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

public class AstarResult {
	public ArrayList<Location> route;
	public int id;
	public double dist;
	public double x;
	public double y;
	public double rating;

	public AstarResult (ArrayList<Location> path, double d, int num, double x, double y) throws JIPSyntaxErrorException, IOException {
		this.route = new ArrayList<>(path);
		this.id = num;
		this.dist = d;
		this.x = x;
		this.y = y;

		JIPEngine jip = new JIPEngine();
		jip.consultFile("taxis.pl");
		JIPTermParser parser = jip.getTermParser();

		JIPQuery jipQuery;
		JIPTerm term;

		jipQuery = jip.openSynchronousQuery(parser.parseTerm("taxis_rating(" + id + ",X)."));
		term = jipQuery.nextSolution();
		if (term != null) {
			this.rating = Double.parseDouble(term.getVariablesTable().get("X").toString());
		}
	}
}
