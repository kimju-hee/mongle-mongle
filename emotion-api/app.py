from flask import Flask, request, jsonify
from flask_cors import CORS
from transformers import AutoTokenizer, AutoModelForSequenceClassification, TextClassificationPipeline

app = Flask(__name__)
CORS(app, resources={r"/predict-emotion": {"origins": "http://localhost:8080"}})

model_name = "searle-j/kote_for_easygoing_people"
tokenizer = AutoTokenizer.from_pretrained(model_name)
model = AutoModelForSequenceClassification.from_pretrained(model_name)

pipe = TextClassificationPipeline(
    model=model,
    tokenizer=tokenizer,
    device=-1,
    return_all_scores=True,
    function_to_apply="sigmoid"
)

@app.route('/predict-emotion', methods=['POST'])
def predict_emotion():
    data = request.get_json()
    text = data.get("content", "")

    if not text:
        return jsonify({"error": "No content"}), 400

    results = pipe(text)[0]
    top_emotion = max(results, key=lambda x: x["score"])

    return jsonify({"emotion": top_emotion["label"], "score": round(top_emotion["score"], 4)})

if __name__ == '__main__':
    app.run(port=5001)
