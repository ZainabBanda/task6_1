import os
import re
import requests
from flask import Flask, request, jsonify
from flask_cors import CORS
from dotenv import load_dotenv

load_dotenv()
app = Flask(__name__)
CORS(app)

HF_API_TOKEN = os.getenv("HF_API_TOKEN")
HEADERS = {"Authorization": f"Bearer {HF_API_TOKEN}"}
API_URL = "https://router.huggingface.co/novita/v3/openai/chat/completions"
MODEL = "deepseek/deepseek-v3-0324"

def fetch_quiz_from_llama(topic: str) -> str:
    payload = {
        "model": MODEL,
        "messages": [{
            "role": "user",
            "content": (
                f"Generate a quiz with 3 questions on “{topic}”. "
                "Each question should have 4 options (A–D) with exactly one correct. "
                "Format exactly as:\n"
                "**QUESTION 1:** …?\n"
                "**OPTION A:** …\n"
                "**OPTION B:** …\n"
                "**OPTION C:** …\n"
                "**OPTION D:** …\n"
                "**ANS:** [A/B/C/D]\n\n"
                "**QUESTION 2:** … (same)\n\n"
                "**QUESTION 3:** … (same)\n"
            )
        }],
        "max_tokens": 500,
        "temperature": 0.7,
        "top_p": 0.9,
    }

    resp = requests.post(API_URL, headers=HEADERS, json=payload)
    if resp.status_code != 200:
        raise RuntimeError(f"HF API error {resp.status_code}: {resp.text}")
    return resp.json()["choices"][0]["message"]["content"]

def process_quiz(text: str) -> list[dict]:
    pattern = re.compile(
        r"\*\*QUESTION \d+:\*\* (.+?)\n"
        r"\*\*OPTION A:\*\* (.+?)\n"
        r"\*\*OPTION B:\*\* (.+?)\n"
        r"\*\*OPTION C:\*\* (.+?)\n"
        r"\*\*OPTION D:\*\* (.+?)\n"
        r"\*\*ANS:\*\* (.+?)(?=\n|$)",
        re.DOTALL
    )
    matches = pattern.findall(text)
    if not matches:
        raise ValueError("Failed to extract quiz with regex")
    return [
        {
            "question": q.strip(),
            "options": [a.strip(), b.strip(), c.strip(), d.strip()],
            "correct_answer": ans.strip()
        }
        for q, a, b, c, d, ans in matches
    ]

@app.route("/getQuiz", methods=["GET"])
def get_quiz():
    topic = request.args.get("topic", "").strip()
    if not topic:
        return jsonify({"error": "Missing `topic` query parameter"}), 400
    try:
        raw = fetch_quiz_from_llama(topic)
        quiz = process_quiz(raw)
        return jsonify({"quiz": quiz})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route("/test", methods=["GET"])
def test():
    return jsonify({"quiz": "test"}), 200

if __name__ == "__main__":
    import argparse
    parser = argparse.ArgumentParser()
    parser.add_argument("--port", type=int, default=5000)
    args = parser.parse_args()

    print(f"🟢 App running on port {args.port}")
    app.run(host="0.0.0.0", port=args.port)
