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
    form = ImageForm()
    return render_template('index.html', form=form)


@app.route('/image-search', methods=['POST'])
def image_search():
    form = ImageForm()
    if form.validate_on_submit():
        image_filename = secure_filename(form.image.data.filename)
        _, tmp_filename = tempfile.mkstemp(suffix="image")
        form.image.data.save(tmp_filename)
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
        form.validate_on_submit()

    return render_template('index.html', form=form, 
                           image_results=results,
                           image_filename=image_filename)


class ImageForm(Form):
    image = FileField(u'Image File')
    submit_button = SubmitField('Search')

if __name__ == '__main__':
    app.run(debug=True, host="127.0.0.1", port=8000)
