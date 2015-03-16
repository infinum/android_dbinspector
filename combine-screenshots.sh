#/bin/bash

MONTAGE=`which montage`
if [ "$MONTAGE" == "" ]; then
    echo "You need imagemagick to use this! Install like this on OS X:"
    echo "brew install imagemagick"
    exit 2
fi

if [ $# -lt 3 ]; then
    echo "Usage: $0 <input_image_1> <input_image_2> [input_image_3..n] <output_image>"
    exit 1
fi

montage -geometry +2+2 -border +2+2 "$@"

exit $?
