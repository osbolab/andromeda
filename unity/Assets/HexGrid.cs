using UnityEngine;
using System.Collections;

public class HexGrid : MonoBehaviour {

	// Use this for initialization
	void Start () {
		hex = GetComponent<MeshFilter> ().mesh;

		hex.Clear ();
		hex.name = "HexTile";
		hex.vertices = verts;
		hex.uv = uvs;
		hex.triangles = tris;
		hex.Optimize ();
		hex.RecalculateNormals ();
		hex.RecalculateBounds ();
	}
	
	// Update is called once per frame
	void Update () {
		time += Time.deltaTime;
		if (time >= 0.9f) {
			rows = cols + 1;
		}
		if (time >= 1.2f) {
			cols += 1;
			time = 0f;
		}

		if (rows >= 6)
			rows = cols = 0;

		Vector3 translate = Vector3.zero;
		for (int row = 0; row < rows; ++row) {
			translate.x = row;
			translate.z = 1.75f * row;
			for (int col = 0; col < cols; ++col) {
				Graphics.DrawMesh (hex, translate, Quaternion.identity, material, 0);
				translate.x += 2f;
			}
		}
	}

	private Mesh hex;
	public Material material;
	public int rows = 0;
	public int cols = 0;
	private float time;

	private Vector3[] verts = new Vector3[] { 
		new Vector3(-1f, 0f, 0.5f), new Vector3(0f, 0f, 1.25f), new Vector3(1f, 0f, 0.5f),
		new Vector3(1f, 0f, -0.5f), new Vector3(0f, 0f, -1.25f), new Vector3(-1f, 0f, -0.5f)
	};
	private Vector2[] uvs = new Vector2[] {
		new Vector2 (0f, 0.75f), new Vector2 (0.5f, 1f), new Vector2 (1f, 0.75f),
		new Vector2 (1f, 0.25f), new Vector2 (0.5f, 0f), new Vector2 (0f, 0.25f)
	};

	private int[] tris = new int[] {
		0, 1, 2,
		0, 2, 5,
		2, 3, 5,
		3, 4, 5
	};
}
