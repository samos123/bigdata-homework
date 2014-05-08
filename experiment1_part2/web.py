import tempfile

from flask import Flask, render_template, request
from flask_bootstrap import Bootstrap
from flask_wtf import Form
from flask_wtf.file import FileField, FileAllowed, FileRequired
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
        tmp_file, tmp_filename = tempfile.mkstemp()
        form.image.data.save(tmp_filename)
        filename = tmp_filename
    else:
        filename = None
        form.validate_on_submit()

    results = []
    return render_template('index.html', form=form, results=results)


class ImageForm(Form):
    image = FileField(u'Image File')
    submit_button = SubmitField('Submit')

if __name__ == '__main__':
    app.run(debug=True, host="127.0.0.1", port=8000)
