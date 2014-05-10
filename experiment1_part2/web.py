import os
import tempfile
import subprocess

from flask import Flask, render_template, request
from flask_bootstrap import Bootstrap
from flask_wtf import Form
from flask_wtf.file import FileField, FileAllowed, FileRequired
from werkzeug import secure_filename
from wtforms import SubmitField, validators
from wtforms.validators import Required


app = Flask(__name__)
# in a real app, these should be configured through Flask-Appconfig
app.config['SECRET_KEY'] = 'devkey'
Bootstrap(app)


@app.route('/', methods=['GET', 'POST'])
def index():
    image_form = ImageForm()
    audio_form = ImageForm()
    return render_template('index.html', image_form=image_form,
                           audio_form=audio_form)


@app.route('/image-search', methods=['GET', 'POST'])
def image_search():
    image_form = ImageForm()
    if image_form.validate_on_submit():
        image_filename = image_form.image.data.filename
        _, tmp_filename = tempfile.mkstemp(suffix="image")
        image_form.image.data.save(tmp_filename)
        results = subprocess.check_output(['./search.sh', tmp_filename])
        print results
        results = results.split('\n')
        if not results[-1]: results = results[:-1]
        results = [row.split(',') for row in results]
        results = [(row[0].decode("utf-8"), row[1]) for row in results]
        results.reverse()
        os.remove(tmp_filename)
    else:
        results = []
        image_filename = None
        image_form.validate_on_submit()

    audio_form = AudioForm()
    return render_template('index.html', image_form=image_form,
                           audio_form=audio_form,
                           image_results=results,
                           image_filename=image_filename)


@app.route('/audio-search', methods=['GET', 'POST'])
def audio_search():
    audio_form = AudioForm()
    if audio_form.validate_on_submit():
        audio_filename = audio_form.audio.data.filename
        _, tmp_filename = tempfile.mkstemp(suffix="audio")
        audio_form.audio.data.save(tmp_filename)
        results = subprocess.check_output(['./search-audio.sh', tmp_filename])
        print results
        results = results.split('\n')
        if not results[-1]: results = results[:-1]
        results = [row.split(':') for row in results]
        results = [(row[0].decode("utf-8"), int(row[1])) for row in results]
        results = sorted(results, key=lambda x: x[1])
        os.remove(tmp_filename)
    else:
        results = []
        audio_filename = None
        audio_form.validate_on_submit()

    image_form = ImageForm()
    return render_template('index.html', image_form=image_form,
                           audio_form=audio_form,
                           audio_results=results,
                           audio_filename=audio_filename)


class ImageForm(Form):
    image = FileField(u'Image File')
    submit_button = SubmitField('Search')


class AudioForm(Form):
    audio = FileField(u'Audio File')
    submit_button = SubmitField('Search')


if __name__ == '__main__':
    app.run(debug=True, host="127.0.0.1", port=8000)
